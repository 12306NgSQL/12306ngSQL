package org.ng12306.ngsql.route.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.Map;

import org.ng12306.ngsql.parser.ast.expression.Expression;
import org.ng12306.ngsql.parser.recognizer.Token;
import org.ng12306.ngsql.parser.recognizer.lexer.SQLLexer;
import org.ng12306.ngsql.route.config.TableRuleConfig.RuleConfig;
import org.ng12306.ngsql.util.ConfigException;
import org.ng12306.ngsql.util.ConfigUtil;
import org.ng12306.ngsql.util.SplitUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*-
 * 加载rule.xml
 * @author:Fredric 
 * @date: 2013-7-20
 */
public class RuleLoader {
    
	private final Map<String, TableRuleConfig> tableRules;
	//private final FunctionManager functionManager;
	
   public RuleLoader() {
        this.tableRules = new HashMap<String, TableRuleConfig>();
        //this.functionManager = new FunctionManager(true);
        this.load();
    }
	
	private void load() {
        InputStream dtd = null;
        InputStream xml = null;
        try {
            dtd = RuleLoader.class.getResourceAsStream("/rule.dtd");
            xml = RuleLoader.class.getResourceAsStream("/rule.xml");
            Element root = ConfigUtil.getDocument(dtd, xml).getDocumentElement();
            loadFunctions(root);
            loadTableRules(root);
        } catch (ConfigException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigException(e);
        } finally {
            if (dtd != null) {
                try {
                    dtd.close();
                } catch (IOException e) {
                }
            }
            if (xml != null) {
                try {
                    xml.close();
                } catch (IOException e) {
                }
            }
        }
    }
	
	 private void loadFunctions(Element root){
		 
	 }
	
	 private void loadTableRules(Element root) throws SQLSyntaxErrorException{
	        NodeList list = root.getElementsByTagName("tableRule");
	        for (int i = 0, n = list.getLength(); i < n; i++) {
	            Node node = list.item(i);
	            if (node instanceof Element) {
	                Element e = (Element) node;
	                String name = e.getAttribute("name");
	                if (tableRules.containsKey(name)) {
	                    throw new ConfigException("table rule " + name + " duplicated!");
	                }
	                NodeList ruleList = e.getElementsByTagName("rule");
	                int length = ruleList.getLength();
	                RuleConfig[] rules = new RuleConfig[length];
	                for (int j = 0; j < length; ++j) {
	                    rules[j] = loadRule((Element) ruleList.item(j));
	                }
	                
	                tableRules.put(name, new TableRuleConfig(name, rules));
	            }
	        } 
	 }
	 
	 private RuleConfig loadRule(Element element) throws SQLSyntaxErrorException{
	        Element columnsEle = ConfigUtil.loadElement(element, "columns");
	        String[] columns = SplitUtil.split(columnsEle.getTextContent(), ',', true);
	        for (int i = 0; i < columns.length; ++i) {
	            columns[i] = columns[i].toUpperCase();
	        }
	        Element algorithmEle = ConfigUtil.loadElement(element, "algorithm");
	        String algorithm = algorithmEle.getTextContent();
	        SQLLexer lexer = new SQLLexer(algorithm);
	        //SQLExprParser parser = new SQLExprParser(lexer, functionManager, false, SQLParser.DEFAULT_CHARSET);
	        //Expression expression = parser.expression();
	        Expression expression = null; //TBD
	        if (lexer.token() != Token.EOF) {
	            throw new ConfigException("route algorithm not end with EOF: " + algorithm);
	        }
	        return new RuleConfig(columns, expression);		 
	 }	 
}
