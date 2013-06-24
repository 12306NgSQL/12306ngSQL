/*
 * Copyright 2012-2013 NgSql Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ng12306.ngsql.route;

import java.sql.SQLNonTransientException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ng12306.ngsql.parser.ast.expression.Expression;
import org.ng12306.ngsql.parser.ast.stmt.SQLStatement;
import org.ng12306.ngsql.parser.ast.stmt.dml.DMLInsertReplaceStatement;
import org.ng12306.ngsql.parser.recognizer.SQLParserDelegate;
import org.ng12306.ngsql.parser.recognizer.syntax.SQLParser;
import org.ng12306.ngsql.parser.visitor.MySQLOutputASTVisitor;
import org.ng12306.ngsql.route.config.SchemaConfig;
import org.ng12306.ngsql.route.config.TableConfig;
import org.ng12306.ngsql.route.config.TableRuleConfig;
import org.ng12306.ngsql.route.config.TableRuleConfig.RuleConfig;
import org.ng12306.ngsql.route.visitor.PartitionKeyVisitor;

/*-
 * @author:Fredric
 * @date: 2013-5-10
 */
public final class ServerRouter {
	
	public static RouteResultset route(SchemaConfig schema, String charset,String stmt) 
			throws SQLNonTransientException{
		
		RouteResultset rrs = new RouteResultset(stmt);
		
		/*生成AST，通过SQLLexer的词法分析和parse的语法分析*/
        SQLStatement ast = SQLParserDelegate.parse(stmt, charset == null ? SQLParser.DEFAULT_CHARSET : charset);
        PartitionKeyVisitor visitor = new PartitionKeyVisitor(schema.getTables());
        //visitor.setTrimSchema(schema.getName());
        ast.accept(visitor);
		
		//匹配规则
		TableConfig matchedTable = null;
		RuleConfig rule = null;
		Map<String, List<Object>> columnValues = null;
		
        Map<String, Map<String, List<Object>>> astExt = visitor.getColumnValue();
        Map<String, TableConfig> tables = schema.getTables();
        
        for(Entry<String, Map<String, List<Object>>> e : astExt.entrySet()){
        	Map<String, List<Object>> col2Val = e.getValue();
        	TableConfig tc = tables.get(e.getKey());
            if (tc == null) {
                continue;
            }
            if (matchedTable == null) {
                matchedTable = tc;
            }
            if (col2Val == null || col2Val.isEmpty()) {
                continue;
            }
            
            TableRuleConfig tr = tc.getRules();
            if(null != tc){
            	for(RuleConfig rc:tr.getRules()){
            		boolean match = true;
            		for(String ruleColumn  :rc.getColumns()){
            			match &= col2Val.containsKey(ruleColumn);
            		}
            		
            		//找到相应表匹配的规则
            		if(match){
                        columnValues = col2Val;
                        rule = rc;
                        matchedTable = tc;
                        break;
            		}
            	}
            }
        }
		
        
		//路由处理
        validateAST(ast, matchedTable, rule, visitor);
        Map<Integer, List<Object[]>> dnMap = ruleCalculate(matchedTable, rule, columnValues);
        if (dnMap == null || dnMap.isEmpty()) {
            throw new IllegalArgumentException("No target dataNode for rule " + rule);
        }
        
        //分库
        if (dnMap.size() == 1) {
            String dataNode = matchedTable.getDataNodes()[dnMap.keySet().iterator().next()];
            String sql = visitor.isSchemaTrimmed() ? genSQL(ast, stmt) : stmt;
            RouteResultsetNode[] rn = new RouteResultsetNode[1];
            rn[0] = new RouteResultsetNode(dataNode, sql);
            rrs.setNodes(rn);
        } else {
            RouteResultsetNode[] rn = new RouteResultsetNode[dnMap.size()];
            if (ast instanceof DMLInsertReplaceStatement) {
                DMLInsertReplaceStatement ir = (DMLInsertReplaceStatement) ast;
                dispatchInsertReplace(rn, ir, rule.getColumns(), dnMap, matchedTable, stmt, visitor);
            } else {
                dispatchWhereBasedStmt(rn, ast, rule.getColumns(), dnMap, matchedTable, stmt, visitor);
            }
            rrs.setNodes(rn);
            setGroupFlagAndLimit(rrs, visitor);
        }
				
		return rrs;
	}
	
	private static void validateAST(SQLStatement ast, TableConfig tc, RuleConfig rule, PartitionKeyVisitor visitor)
            throws SQLNonTransientException{
		
	}
	
	private static Map<Integer, List<Object[]>> ruleCalculate(TableConfig matchedTable,
            RuleConfig rule,
            Map<String, List<Object>> columnValues){
		Map<Integer, List<Object[]>> map = new HashMap<Integer, List<Object[]>>(1, 1);
		Expression algorithm = rule.getAlgorithm();
		String[] cols = rule.getColumns();
		
        Map<String, Object> parameter = new HashMap<String, Object>(cols.length, 1);
        ArrayList<Iterator<Object>> colsValIter = new ArrayList<Iterator<Object>>(columnValues.size());
        for (String rc : cols) {
            List<Object> list = columnValues.get(rc);
            if (list == null) {
                String msg = "route err: rule column " + rc + " dosn't exist in extract: " + columnValues;
                throw new IllegalArgumentException(msg);
            }
            colsValIter.add(list.iterator());
        }
        
        for(Iterator<Object> mainIter = colsValIter.get(0); mainIter.hasNext();){
            Object[] tuple = new Object[cols.length];
            for (int i = 0; i < cols.length; ++i) {
                Object value = colsValIter.get(i).next();
                tuple[i] = value;
                parameter.put(cols[i], value);
            }
            
            Integer[] dataNodeIndexes = calcDataNodeIndexesByFunction(algorithm, parameter);

            for (int i = 0; i < dataNodeIndexes.length; ++i) {
                Integer dataNodeIndex = dataNodeIndexes[i];
                List<Object[]> list = map.get(dataNodeIndex);
                if (list == null) {
                    list = new LinkedList<Object[]>();
                    map.put(dataNodeIndex, list);
                }
                list.add(tuple);
            }
        }
			
		return null;
	}
	
    private static Integer[] calcDataNodeIndexesByFunction(Expression algorithm, Map<String, Object> parameter) {
        Integer[] dataNodeIndexes;
        Object calRst = algorithm.evaluation(parameter);
        if (calRst instanceof Number) {
            dataNodeIndexes = new Integer[1];
            dataNodeIndexes[0] = ((Number) calRst).intValue();
        } else if (calRst instanceof Integer[]) {
            dataNodeIndexes = (Integer[]) calRst;
        } else if (calRst instanceof int[]) {
            int[] intArray = (int[]) calRst;
            dataNodeIndexes = new Integer[intArray.length];
            for (int i = 0; i < intArray.length; ++i) {
                dataNodeIndexes[i] = intArray[i];
            }
        } else {
            throw new IllegalArgumentException("route err: result of route function is wrong type or null: " + calRst);
        }
        return dataNodeIndexes;
    	   	
    }
	
    private static String genSQL(SQLStatement ast, String orginalSql) {
        StringBuilder s = new StringBuilder();
        ast.accept(new MySQLOutputASTVisitor(s));
        return s.toString();
    }
    
    private static void dispatchInsertReplace(RouteResultsetNode[] rn,
            DMLInsertReplaceStatement stmt,
            String[] ruleColumns,
            Map<Integer, List<Object[]>> dataNodeMap,
            TableConfig matchedTable,
            String originalSQL,
            PartitionKeyVisitor visitor) {
    	
    }
    
    private static void dispatchWhereBasedStmt(RouteResultsetNode[] rn,
            SQLStatement stmtAST,
            String[] ruleColumns,
            Map<Integer, List<Object[]>> dataNodeMap,
            TableConfig matchedTable,
            String originalSQL,
            PartitionKeyVisitor visitor){
    	
    }
    
    private static void setGroupFlagAndLimit(RouteResultset rrs, PartitionKeyVisitor visitor) {

    }
    

	
}
