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
