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
package org.ng12306.sql.server;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.ng12306.ngsql.model.DataSourceConfig;
import org.ng12306.ngsql.model.QuarantineConfig;
import org.ng12306.ngsql.model.SchemaConfig;
import org.ng12306.ngsql.model.SystemConfig;
import org.ng12306.ngsql.model.UserConfig;
import org.ng12306.ngsql.route.config.RuleLoader;
import org.ng12306.ngsql.route.config.TableRuleConfig;
import org.ng12306.ngsql.util.ConfigUtil;
import org.ng12306.ngsql.util.SplitUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/*-
 * 加载server.xml和schema.xml中的撇脂项
 * 2013-8-7
 * Fredric
 */
public final class ConfigLoader {
	
	private InputStream serverxml;
	private InputStream serverdtd;
	private InputStream schemaxml;
	private InputStream schemadtd;
	private Element root;
	private Element schemaroot;
	
	private final SystemConfig system;
	private final Map<String, UserConfig> users;
	private Map<String, SchemaConfig> schemas;
	private Map<String, DataSourceConfig> dataSources;
	private final Map<String, TableRuleConfig> tableRules;
	
	public ConfigLoader(){
		this.system = new SystemConfig();
		this.users  = new HashMap<String, UserConfig>();
		this.schemas= new HashMap<String, SchemaConfig>();
		this.dataSources = new HashMap<String, DataSourceConfig>();
		this.tableRules  = new HashMap<String, TableRuleConfig>();
		
		serverdtd = RuleLoader.class.getResourceAsStream("server.dtd");
        serverxml = RuleLoader.class.getResourceAsStream("server.xml");
        
		schemadtd = RuleLoader.class.getResourceAsStream("schema.dtd");
        schemaxml = RuleLoader.class.getResourceAsStream("schema.xml");
        
        
        try {
			root = ConfigUtil.getDocument(serverdtd, serverxml).getDocumentElement();
			schemaroot = ConfigUtil.getDocument(schemadtd, schemaxml).getDocumentElement();
			//loadSystemConfig();
			loadUserConfigs();
			loadSchemaConfigs();
			loadDataSource();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void loadSystemConfig() throws NoSuchMethodException, SecurityException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        NodeList list = root.getElementsByTagName("system");
        Node node = list.item(0);
        Map<String, Object> props = ConfigUtil.loadElements((Element) node);
        
        Iterator<?> it = props.entrySet().iterator();
        
        while (it.hasNext()) { 
        	@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) it.next(); 
        	Object key = entry.getKey(); 
        	Object value = entry.getValue();
        	
        	Class.forName("org.ng12306.sql.ConfigLoader")
        			.getMethod("set"+key.toString(),String.class)
        			.invoke(this.getSystemConfig(), value);
        	
        }
	}
		
	public SystemConfig getSystemConfig(){
		return system;
	}
	
	public void loadUserConfigs(){
        NodeList list = root.getElementsByTagName("user");
        for (int i = 0, n = list.getLength(); i < n; i++) {
            Node node = list.item(i);
            if (node instanceof Element) {
                Element e = (Element) node;
                String name = e.getAttribute("name");
                UserConfig user = new UserConfig();
                user.setName(name);
                Map<String, Object> props = ConfigUtil.loadElements(e);
                user.setPassword((String) props.get("password"));
                String schemas = (String) props.get("schemas");
                if (schemas != null) {
                    String[] strArray = SplitUtil.split(schemas, ',', true);
                    user.setSchemas(new HashSet<String>(Arrays.asList(strArray)));
                }

                users.put(name, user);
            }
        }
	}
	
	public Map<String, UserConfig> getUserConfigs(){
		return users;
	}
	
	void loadSchemaConfigs(){
        NodeList list = schemaroot.getElementsByTagName("schema");
        for (int i = 0, n = list.getLength(); i < n; i++) {
            Element schemaElement = (Element) list.item(i);
            String name = schemaElement.getAttribute("name");
            String dataNode = schemaElement.getAttribute("dataNode");
            // 在非空的情况下检查dataNode是否存在
            if (dataNode != null && dataNode.length() != 0) {
                //checkDataNodeExists(dataNode);
            } else {
                dataNode = "";//确保非空
            }
            String group = "default";
            if (schemaElement.hasAttribute("group")) {
                group = schemaElement.getAttribute("group").trim();
            }
            Map<String, org.ng12306.ngsql.route.config.TableConfig> tables = loadTables(schemaElement);

            boolean keepSqlSchema = false;
            if (schemaElement.hasAttribute("keepSqlSchema")) {
                keepSqlSchema = Boolean.parseBoolean(schemaElement.getAttribute("keepSqlSchema").trim());
            }
            //schemas.put(name, new SchemaConfig(name, dataNode, group, keepSqlSchema, tables));
        }
	}
	
	private Map<String, org.ng12306.ngsql.route.config.TableConfig> loadTables(Element node) {
		Map<String, org.ng12306.ngsql.route.config.TableConfig> tables 
			= new HashMap<String, org.ng12306.ngsql.route.config.TableConfig>();
		
		NodeList nodeList = node.getElementsByTagName("table");
		
		for(int i = 0; i < nodeList.getLength(); i++){
            Element tableElement = (Element) nodeList.item(i);
            String name = tableElement.getAttribute("name").toUpperCase();
            String dataNode = tableElement.getAttribute("dataNode");
            TableRuleConfig tableRule = null;
            
            if(tableElement.hasAttribute("rule")){
                String ruleName = tableElement.getAttribute("rule");
                tableRule = tableRules.get(ruleName);
            }
            
            String[] tableNames = SplitUtil.split(name, ',', true);
            for (String tableName : tableNames) {
            	org.ng12306.ngsql.route.config.TableConfig table
            		= new org.ng12306.ngsql.route.config.TableConfig(tableName, dataNode, tableRule);
                tables.put(table.getName(), table);
            }                    
		}
		
	    return tables;
	}
	
		
	public Map<String, SchemaConfig> getSchemaConfigs(){
		return schemas;
	}
	
    private Element findPropertyByName(Element bean, String name) {
        NodeList propertyList = bean.getElementsByTagName("property");
        for (int j = 0, m = propertyList.getLength(); j < m; ++j) {
            Node node = propertyList.item(j);
            if (node instanceof Element) {
                Element p = (Element) node;
                if (name.equals(p.getAttribute("name"))) {
                    return p;
                }
            }
        }
        return null;
    }
	
	void loadDataSource(){
		NodeList dataSourceList = schemaroot.getElementsByTagName("dataSource");
		for (int i = 0, n = dataSourceList.getLength(); i < n; i++) {		
			Element element = (Element) dataSourceList.item(i);
			String dsNamePrefix = element.getAttribute("name");
			
            Element locElement = findPropertyByName(element, "location");
            NodeList locationList = locElement.getElementsByTagName("location");
            
            Element usr = findPropertyByName(element, "user");
            Element password = findPropertyByName(element, "password");
            Element sqlmode  = findPropertyByName(element, "sqlMode");
            
            for(int j = 0; j < locationList.getLength(); j++){
            	DataSourceConfig channel = new DataSourceConfig();
            	
                String locStr = ((Element) locationList.item(j)).getTextContent();
                int colonIndex = locStr.indexOf(':');
                int slashIndex = locStr.indexOf('/');
                String dsHost = locStr.substring(0, colonIndex).trim();
                String dnNpde = locStr.substring(slashIndex+1, locStr.length());
                int dsPort = Integer.parseInt(locStr.substring(colonIndex + 1, slashIndex).trim());
                
                channel.setHost(dsHost);
                channel.setPort(dsPort);
                channel.setName(usr.getTextContent());
                channel.setPassword(password.getTextContent());
                channel.setSqlMode(sqlmode.getTextContent());
                                   	
            }                 
		}
	}
	
	public Map<String, DataSourceConfig> getDataSources(){
		return dataSources;
	}
	
	public QuarantineConfig getQuarantineConfig(){
		return null;
	}
	
	
}
