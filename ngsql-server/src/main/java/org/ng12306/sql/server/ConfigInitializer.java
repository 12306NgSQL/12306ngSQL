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

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.ng12306.ngsql.model.DataSourceConfig;
import org.ng12306.ngsql.model.QuarantineConfig;
import org.ng12306.ngsql.model.SchemaConfig;
import org.ng12306.ngsql.model.SystemConfig;
import org.ng12306.ngsql.model.UserConfig;
import org.ng12306.ngsql.util.ConfigException;
import org.ng12306.ngsql.util.SplitUtil;

/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-4-6 下午3:16:25
* @version: 1.0
 */
public class ConfigInitializer {

	private volatile SystemConfig system;
    private volatile NgSqlCluster cluster;
    private volatile QuarantineConfig quarantine;
    private volatile Map<String, UserConfig> users;
    private volatile Map<String, SchemaConfig> schemas;
    private volatile Map<String, DataSourceConfig> dataSources;

    public ConfigInitializer() {
    	
    	ConfigLoader configLoader = new ConfigLoader();
        //SchemaLoader schemaLoader = new XMLSchemaLoader();
       // XMLConfigLoader configLoader = new XMLConfigLoader(schemaLoader);
       // try {
            //RouteRuleInitializer.initRouteRule(schemaLoader);
           // schemaLoader = null;
      //  } catch (SQLSyntaxErrorException e) {
       //     throw new ConfigException(e);
      //  }
		this.system 	 = configLoader.getSystemConfig();
		this.users 		 = configLoader.getUserConfigs();
		this.schemas 	 = configLoader.getSchemaConfigs();
		this.dataSources = configLoader.getDataSources();
		this.quarantine  = configLoader.getQuarantineConfig();
//        this.cluster = initCobarCluster(configLoader);
//
//        this.checkConfig();
    }

    private void checkConfig() throws ConfigException {
        if (users == null || users.isEmpty())
            return;
        for (UserConfig uc : users.values()) {
            if (uc == null) {
                continue;
            }
            Set<String> authSchemas = uc.getSchemas();
            if (authSchemas == null) {
                continue;
            }
            for (String schema : authSchemas) {
                if (!schemas.containsKey(schema)) {
                    String errMsg = "schema " + schema + " refered by user " + uc.getName() + " is not exist!";
                    throw new ConfigException(errMsg);
                }
            }
        }

        for (SchemaConfig sc : schemas.values()) {
            if (null == sc) {
                continue;
            }
            String g = sc.getGroup();
            if (!cluster.getGroups().containsKey(g)) {
                throw new ConfigException("[group:" + g + "] refered by [schema:" + sc.getName() + "] is not exist!");
            }
        }
    }

    public SystemConfig getSystem() {
        return system;
    }

    public NgSqlCluster getCluster() {
        return cluster;
    }

    public QuarantineConfig getQuarantine() {
        return quarantine;
    }

    public Map<String, UserConfig> getUsers() {
        return users;
    }

    public Map<String, SchemaConfig> getSchemas() {
        return schemas;
    }

    public Map<String, DataSourceConfig> getDataSources() {
        return dataSources;
    }

    private void checkDataSourceExists(String... nodes) {
        if (nodes == null || nodes.length < 1) {
            return;
        }
        for (String node : nodes) {
            if (!dataSources.containsKey(node)) {
                throw new ConfigException("dataSource '" + node + "' is not found!");
            }
        }
    }
}
