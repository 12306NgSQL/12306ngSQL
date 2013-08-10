package org.ng12306.sql.server;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
	
	public ConfigLoader(){
		this.system = new SystemConfig();
		this.users  = new HashMap<String, UserConfig>();
		this.schemas= new HashMap<String, SchemaConfig>();
		this.dataSources = new HashMap<String, DataSourceConfig>();
		
		serverdtd = RuleLoader.class.getResourceAsStream("/server.dtd");
        serverxml = RuleLoader.class.getResourceAsStream("/server.xml");
        
		schemadtd = RuleLoader.class.getResourceAsStream("/schema.dtd");
        schemaxml = RuleLoader.class.getResourceAsStream("/schema.xml");
        
        
        try {
			root = ConfigUtil.getDocument(serverdtd, serverxml).getDocumentElement();
			schemaroot = ConfigUtil.getDocument(schemadtd, schemaxml).getDocumentElement();
			loadSystemConfig();
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
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
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
        NodeList list = root.getElementsByTagName("schema");
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
		return null;	
	}
	
		
	public Map<String, SchemaConfig> getSchemaConfigs(){
		return schemas;
	}
	
	void loadDataSource(){
		
	}
	
	public Map<String, DataSourceConfig> getDataSources(){
		return dataSources;
	}
	
	public QuarantineConfig getQuarantineConfig(){
		return null;
	}
	
	
}
