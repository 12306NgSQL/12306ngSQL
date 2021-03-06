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
package org.ng12306.ngsql.route.visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ng12306.ngsql.parser.ast.ASTNode;
import org.ng12306.ngsql.parser.ast.expression.Expression;
import org.ng12306.ngsql.parser.ast.expression.BinaryOperatorExpression;
import org.ng12306.ngsql.parser.ast.expression.PolyadicOperatorExpression;
import org.ng12306.ngsql.parser.ast.expression.ReplacableExpression;
import org.ng12306.ngsql.parser.ast.expression.UnaryOperatorExpression;
import org.ng12306.ngsql.parser.ast.expression.comparison.BetweenAndExpression;
import org.ng12306.ngsql.parser.ast.expression.comparison.ComparisionEqualsExpression;
import org.ng12306.ngsql.parser.ast.expression.comparison.ComparisionIsExpression;
import org.ng12306.ngsql.parser.ast.expression.comparison.ComparisionNullSafeEqualsExpression;
import org.ng12306.ngsql.parser.ast.expression.comparison.InExpression;
import org.ng12306.ngsql.parser.ast.expression.logical.LogicalAndExpression;
import org.ng12306.ngsql.parser.ast.expression.logical.LogicalOrExpression;
import org.ng12306.ngsql.parser.ast.expression.misc.InExpressionList;
import org.ng12306.ngsql.parser.ast.expression.misc.UserExpression;
import org.ng12306.ngsql.parser.ast.expression.primary.CaseWhenOperatorExpression;
import org.ng12306.ngsql.parser.ast.expression.primary.DefaultValue;
import org.ng12306.ngsql.parser.ast.expression.primary.ExistsPrimary;
import org.ng12306.ngsql.parser.ast.expression.primary.Identifier;
import org.ng12306.ngsql.parser.ast.expression.primary.MatchExpression;
import org.ng12306.ngsql.parser.ast.expression.primary.ParamMarker;
import org.ng12306.ngsql.parser.ast.expression.primary.PlaceHolder;
import org.ng12306.ngsql.parser.ast.expression.primary.RowExpression;
import org.ng12306.ngsql.parser.ast.expression.primary.SysVarPrimary;
import org.ng12306.ngsql.parser.ast.expression.primary.UsrDefVarPrimary;
import org.ng12306.ngsql.parser.ast.expression.primary.function.FunctionExpression;
import org.ng12306.ngsql.parser.ast.expression.primary.function.cast.Cast;
import org.ng12306.ngsql.parser.ast.expression.primary.function.cast.Convert;
import org.ng12306.ngsql.parser.ast.expression.primary.function.datetime.Extract;
import org.ng12306.ngsql.parser.ast.expression.primary.function.datetime.GetFormat;
import org.ng12306.ngsql.parser.ast.expression.primary.function.datetime.Timestampadd;
import org.ng12306.ngsql.parser.ast.expression.primary.function.datetime.Timestampdiff;
import org.ng12306.ngsql.parser.ast.expression.primary.function.groupby.Avg;
import org.ng12306.ngsql.parser.ast.expression.primary.function.groupby.Count;
import org.ng12306.ngsql.parser.ast.expression.primary.function.groupby.GroupConcat;
import org.ng12306.ngsql.parser.ast.expression.primary.function.groupby.Max;
import org.ng12306.ngsql.parser.ast.expression.primary.function.groupby.Min;
import org.ng12306.ngsql.parser.ast.expression.primary.function.groupby.Sum;
import org.ng12306.ngsql.parser.ast.expression.primary.function.string.Char;
import org.ng12306.ngsql.parser.ast.expression.primary.function.string.Trim;
import org.ng12306.ngsql.parser.ast.expression.primary.literal.IntervalPrimary;
import org.ng12306.ngsql.parser.ast.expression.primary.literal.LiteralBitField;
import org.ng12306.ngsql.parser.ast.expression.primary.literal.LiteralBoolean;
import org.ng12306.ngsql.parser.ast.expression.primary.literal.LiteralHexadecimal;
import org.ng12306.ngsql.parser.ast.expression.primary.literal.LiteralNull;
import org.ng12306.ngsql.parser.ast.expression.primary.literal.LiteralNumber;
import org.ng12306.ngsql.parser.ast.expression.primary.literal.LiteralString;
import org.ng12306.ngsql.parser.ast.expression.string.LikeExpression;
import org.ng12306.ngsql.parser.ast.expression.type.CollateExpression;
import org.ng12306.ngsql.parser.ast.fragment.GroupBy;
import org.ng12306.ngsql.parser.ast.fragment.Limit;
import org.ng12306.ngsql.parser.ast.fragment.OrderBy;
import org.ng12306.ngsql.parser.ast.fragment.ddl.ColumnDefinition;
import org.ng12306.ngsql.parser.ast.fragment.ddl.TableOptions;
import org.ng12306.ngsql.parser.ast.fragment.ddl.datatype.DataType;
import org.ng12306.ngsql.parser.ast.fragment.ddl.index.IndexColumnName;
import org.ng12306.ngsql.parser.ast.fragment.ddl.index.IndexOption;
import org.ng12306.ngsql.parser.ast.fragment.tableref.Dual;
import org.ng12306.ngsql.parser.ast.fragment.tableref.IndexHint;
import org.ng12306.ngsql.parser.ast.fragment.tableref.InnerJoin;
import org.ng12306.ngsql.parser.ast.fragment.tableref.NaturalJoin;
import org.ng12306.ngsql.parser.ast.fragment.tableref.OuterJoin;
import org.ng12306.ngsql.parser.ast.fragment.tableref.StraightJoin;
import org.ng12306.ngsql.parser.ast.fragment.tableref.SubqueryFactor;
import org.ng12306.ngsql.parser.ast.fragment.tableref.TableRefFactor;
import org.ng12306.ngsql.parser.ast.fragment.tableref.TableReference;
import org.ng12306.ngsql.parser.ast.fragment.tableref.TableReferences;
import org.ng12306.ngsql.parser.ast.stmt.dal.DALSetCharacterSetStatement;
import org.ng12306.ngsql.parser.ast.stmt.dal.DALSetNamesStatement;
import org.ng12306.ngsql.parser.ast.stmt.dal.DALSetStatement;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowAuthors;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowBinLogEvent;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowBinaryLog;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowCharaterSet;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowCollation;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowColumns;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowContributors;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowCreate;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowDatabases;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowEngine;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowEngines;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowErrors;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowEvents;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowFunctionCode;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowFunctionStatus;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowGrants;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowIndex;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowMasterStatus;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowOpenTables;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowPlugins;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowPrivileges;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowProcedureCode;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowProcedureStatus;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowProcesslist;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowProfile;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowProfiles;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowSlaveHosts;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowSlaveStatus;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowStatus;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowTableStatus;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowTables;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowTriggers;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowVariables;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowWarnings;
import org.ng12306.ngsql.parser.ast.stmt.ddl.DDLAlterTableStatement;
import org.ng12306.ngsql.parser.ast.stmt.ddl.DDLAlterTableStatement.AlterSpecification;
import org.ng12306.ngsql.parser.ast.stmt.ddl.DDLCreateIndexStatement;
import org.ng12306.ngsql.parser.ast.stmt.ddl.DDLCreateTableStatement;
import org.ng12306.ngsql.parser.ast.stmt.ddl.DDLDropIndexStatement;
import org.ng12306.ngsql.parser.ast.stmt.ddl.DDLDropTableStatement;
import org.ng12306.ngsql.parser.ast.stmt.ddl.DDLRenameTableStatement;
import org.ng12306.ngsql.parser.ast.stmt.ddl.DDLTruncateStatement;
import org.ng12306.ngsql.parser.ast.stmt.ddl.DescTableStatement;
import org.ng12306.ngsql.parser.ast.stmt.dml.DMLCallStatement;
import org.ng12306.ngsql.parser.ast.stmt.dml.DMLDeleteStatement;
import org.ng12306.ngsql.parser.ast.stmt.dml.DMLInsertStatement;
import org.ng12306.ngsql.parser.ast.stmt.dml.DMLReplaceStatement;
import org.ng12306.ngsql.parser.ast.stmt.dml.DMLSelectStatement;
import org.ng12306.ngsql.parser.ast.stmt.dml.DMLSelectUnionStatement;
import org.ng12306.ngsql.parser.ast.stmt.dml.DMLUpdateStatement;
import org.ng12306.ngsql.parser.ast.stmt.extension.ExtDDLCreatePolicy;
import org.ng12306.ngsql.parser.ast.stmt.extension.ExtDDLDropPolicy;
import org.ng12306.ngsql.parser.ast.stmt.mts.MTSReleaseStatement;
import org.ng12306.ngsql.parser.ast.stmt.mts.MTSRollbackStatement;
import org.ng12306.ngsql.parser.ast.stmt.mts.MTSSavepointStatement;
import org.ng12306.ngsql.parser.ast.stmt.mts.MTSSetTransactionStatement;
import org.ng12306.ngsql.parser.util.Pair;
import org.ng12306.ngsql.parser.visitor.SQLASTVisitor;
import org.ng12306.ngsql.route.config.TableConfig;

/*-
 * 支持字符类型的ID，按照字符数值的区间分区，实现单维路由
 * @author:Fredric
 * @date: 2013-5-17
 */
public class PartitionKeyVisitor implements SQLASTVisitor{
    
	private static final Set<Class<? extends Expression>> VERDICT_PASS_THROUGH_WHERE =
            new HashSet<Class<? extends Expression>>(6);
    private static final Set<Class<? extends Expression>> GROUP_FUNC_PASS_THROUGH_SELECT =
            new HashSet<Class<? extends Expression>>(5);
    private static final Set<Class<? extends Expression>> PARTITION_OPERAND_SINGLE =
            new HashSet<Class<? extends Expression>>(3);
    
    static {
        VERDICT_PASS_THROUGH_WHERE.add(LogicalAndExpression.class);
        VERDICT_PASS_THROUGH_WHERE.add(LogicalOrExpression.class);
        VERDICT_PASS_THROUGH_WHERE.add(BetweenAndExpression.class);
        VERDICT_PASS_THROUGH_WHERE.add(InExpression.class);
        VERDICT_PASS_THROUGH_WHERE.add(ComparisionNullSafeEqualsExpression.class);
        VERDICT_PASS_THROUGH_WHERE.add(ComparisionEqualsExpression.class);
        GROUP_FUNC_PASS_THROUGH_SELECT.add(Count.class);
        GROUP_FUNC_PASS_THROUGH_SELECT.add(Sum.class);
        GROUP_FUNC_PASS_THROUGH_SELECT.add(Min.class);
        GROUP_FUNC_PASS_THROUGH_SELECT.add(Max.class);
        PARTITION_OPERAND_SINGLE.add(BetweenAndExpression.class);
        PARTITION_OPERAND_SINGLE.add(ComparisionNullSafeEqualsExpression.class);
        PARTITION_OPERAND_SINGLE.add(ComparisionEqualsExpression.class);
    }
	
    private static boolean isVerdictPassthroughWhere(Expression node) {
        if (node == null) return false;
        return VERDICT_PASS_THROUGH_WHERE.contains(node.getClass());
    }

    private static boolean isGroupFuncPassthroughSelect(Expression node) {
        if (node == null) return false;
        return GROUP_FUNC_PASS_THROUGH_SELECT.contains(node.getClass());
    }
    
    public static boolean isPartitionKeyOperandSingle(Expression expr, ASTNode parent) {
        return parent == null
               && expr instanceof ReplacableExpression
               && PARTITION_OPERAND_SINGLE.contains(expr.getClass());
    }
    
    public static boolean isPartitionKeyOperandIn(Expression expr, ASTNode parent) {
        return expr != null && parent instanceof InExpression;
    }
    
    public static final int GROUP_CANCEL = -1;
    public static final int GROUP_NON = 0;
    public static final int GROUP_SUM = 1;
    public static final int GROUP_MIN = 2;
    public static final int GROUP_MAX = 3;
    private boolean verdictGroupFunc = true;
    private int groupFuncType = GROUP_NON;
    private boolean verdictColumn = true;
    private long limitSize = -1L;
    private int idLevel = 2;
	private final Map<String, TableConfig> tablesRuleConfig;
	private Map<String, Map<String, List<Object>>> columnValue 
		= new HashMap<String, Map<String, List<Object>>>(2, 1);
	
	private final Map<Object, Object> evaluationParameter = Collections.emptyMap();
	
	private Map<String, Map<String, Map<Object, Set<Pair<Expression, ASTNode>>>>> columnValueIndex;
	
    public PartitionKeyVisitor(Map<String, TableConfig> tables) {
        if (tables == null || tables.isEmpty()) {
            tables = Collections.emptyMap();
        }
        this.tablesRuleConfig = tables;
    }
	
    private boolean schemaTrimmed = false;
	/*-
	 * 处理insert操作
	 */
	public void visit() {
		
	}
	
    public Map<String, Map<String, List<Object>>> getColumnValue() {
        return columnValue;
    }

	public boolean isSchemaTrimmed() {
		// TODO Auto-generated method stub
		return schemaTrimmed;
	}
	
	
    public Map<String, Map<Object, Set<Pair<Expression, ASTNode>>>> getColumnIndex(String tableNameUp) {
        if (columnValueIndex == null) return Collections.emptyMap();
        Map<String, Map<Object, Set<Pair<Expression, ASTNode>>>> index = columnValueIndex.get(tableNameUp);
        if (index == null || index.isEmpty()) return Collections.emptyMap();
        return index;
    }

    //**************************************************
    private void limit(Limit limit) {
        if (limit != null) {
            Object lo = limit.getSize();
            if (lo instanceof Expression) lo = ((Expression) lo).evaluation(evaluationParameter);
            if (lo instanceof Number) limitSize = ((Number) lo).longValue();
        }
    }
    
    private void visitChild(int idLevel, boolean verdictColumn, boolean verdictGroupFunc, ASTNode... nodes){
        if (nodes == null || nodes.length <= 0) return;
        int oldLevel = this.idLevel;
        boolean oldVerdict = this.verdictColumn;
        boolean oldverdictGroupFunc = this.verdictGroupFunc;
        this.idLevel = idLevel;
        this.verdictColumn = verdictColumn;
        this.verdictGroupFunc = verdictGroupFunc;
        try {
            for (ASTNode node : nodes) {
                if (node != null) node.accept(this);
            }
        } finally {
            this.verdictColumn = oldVerdict;
            this.idLevel = oldLevel;
            this.verdictGroupFunc = oldverdictGroupFunc;
        }
    }
    
    private void visitChild(int idLevel, boolean verdictColumn, boolean verdictGroupFunc, List<? extends ASTNode> nodes){
        if (nodes == null || nodes.isEmpty()) return;
        int oldLevel = this.idLevel;
        boolean oldVerdict = this.verdictColumn;
        boolean oldverdictGroupFunc = this.verdictGroupFunc;
        this.idLevel = idLevel;
        this.verdictColumn = verdictColumn;
        this.verdictGroupFunc = verdictGroupFunc;
        try {
            for (ASTNode node : nodes) {
                if (node != null) node.accept(this);
            }
        } finally {
            this.verdictColumn = oldVerdict;
            this.idLevel = oldLevel;
            this.verdictGroupFunc = oldverdictGroupFunc;
        }
    }
    
	@Override
	public void visit(DMLSelectStatement node) {
		// TODO Auto-generated method stub
		boolean verdictGroup = true;
		
		List<Expression> exprList = node.getSelectExprListWithoutAlias();
        if (verdictGroupFunc) {
            for (Expression expr : exprList) {
                if (!isGroupFuncPassthroughSelect(expr)) {
                    groupFuncType = GROUP_CANCEL;
                    verdictGroup = false;
                    break;
                }
            }
            limit(node.getLimit());
        }
        visitChild(2, false, verdictGroupFunc && verdictGroup, exprList);

        TableReference tr = node.getTables();
        visitChild(1, verdictColumn, verdictGroupFunc && verdictGroup, tr);

        Expression where = node.getWhere();
        visitChild(2, verdictColumn, false, where);

        GroupBy group = node.getGroup();
        visitChild(2, false, false, group);

        Expression having = node.getHaving();
        visitChild(2, verdictColumn, false, having);

        OrderBy order = node.getOrder();
        visitChild(2, false, false, order);
	}
    
    
    
	@Override
	public void visit(BetweenAndExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ComparisionIsExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(InExpressionList node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LikeExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CollateExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(UserExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(UnaryOperatorExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BinaryOperatorExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(PolyadicOperatorExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LogicalAndExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LogicalOrExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ComparisionEqualsExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ComparisionNullSafeEqualsExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(InExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(FunctionExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Char node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Convert node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Trim node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Cast node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Avg node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Max node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Min node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Sum node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Count node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(GroupConcat node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Extract node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Timestampdiff node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Timestampadd node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(GetFormat node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IntervalPrimary node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LiteralBitField node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LiteralBoolean node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LiteralHexadecimal node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LiteralNull node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LiteralNumber node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LiteralString node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CaseWhenOperatorExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DefaultValue node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ExistsPrimary node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(PlaceHolder node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Identifier node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(MatchExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ParamMarker node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RowExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SysVarPrimary node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(UsrDefVarPrimary node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IndexHint node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(InnerJoin node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NaturalJoin node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(OuterJoin node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(StraightJoin node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SubqueryFactor node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TableReferences node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TableRefFactor node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Dual dual) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(GroupBy node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Limit node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(OrderBy node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ColumnDefinition node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IndexOption node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IndexColumnName node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TableOptions node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AlterSpecification node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DataType node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowAuthors node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowBinaryLog node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowBinLogEvent node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowCharaterSet node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowCollation node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowColumns node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowContributors node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowCreate node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowDatabases node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowEngine node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowEngines node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowErrors node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowEvents node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowFunctionCode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowFunctionStatus node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowGrants node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowIndex node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowMasterStatus node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowOpenTables node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowPlugins node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowPrivileges node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowProcedureCode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowProcedureStatus node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowProcesslist node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowProfile node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowProfiles node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowSlaveHosts node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowSlaveStatus node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowStatus node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowTables node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowTableStatus node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowTriggers node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowVariables node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ShowWarnings node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DescTableStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DALSetStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DALSetNamesStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DALSetCharacterSetStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DMLCallStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DMLDeleteStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DMLInsertStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DMLReplaceStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DMLSelectUnionStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DMLUpdateStatement node) {
		// TODO Auto-generated method stub
        TableReference tr = node.getTableRefs();
        visitChild(1, false, false, tr);

        List<Pair<Identifier, Expression>> assignmentList = node.getValues();
        if (assignmentList != null && !assignmentList.isEmpty()) {
            List<ASTNode> list = new ArrayList<ASTNode>(assignmentList.size() * 2);
            for (Pair<Identifier, Expression> p : assignmentList) {
                if (p == null) continue;
                list.add(p.getKey());
                list.add(p.getValue());
            }
            visitChild(2, false, false, list);
        }

        Expression where = node.getWhere();
        visitChild(2, verdictColumn, false, where);

        OrderBy order = node.getOrderBy();
        visitChild(2, false, false, order);
		
	}

	@Override
	public void visit(MTSSetTransactionStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(MTSSavepointStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(MTSReleaseStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(MTSRollbackStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DDLTruncateStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DDLAlterTableStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DDLCreateIndexStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DDLCreateTableStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DDLRenameTableStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DDLDropIndexStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DDLDropTableStatement node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ExtDDLCreatePolicy node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ExtDDLDropPolicy node) {
		// TODO Auto-generated method stub
		
	}
}
