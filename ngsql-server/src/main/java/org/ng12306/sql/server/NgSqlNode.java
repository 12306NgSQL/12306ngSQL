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

import org.apache.log4j.Logger;
import org.ng12306.ngsql.model.NgSqlNodeConfig;

/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-4-5 下午8:28:49
* @version: 1.0
 */
public class NgSqlNode {

	private static final Logger LOGGER = Logger.getLogger(NgSqlNode.class);

    private final String name;
    private NgSqlNodeConfig config;
    
    public NgSqlNode(NgSqlNodeConfig config) {
        this.name = config.getName();
    }

    public String getName() {
        return name;
    }

    public NgSqlNodeConfig getConfig() {
        return config;
    }

}
