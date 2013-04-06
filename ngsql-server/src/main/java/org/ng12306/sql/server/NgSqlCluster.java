package org.ng12306.sql.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ng12306.ngsql.model.NgSqlNodeConfig;

/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-4-5 下午8:26:29
* @version: 1.0
 */
public class NgSqlCluster {

	private Map<String, NgSqlNode> nodes;
    private Map<String, List<String>> groups;

    public NgSqlCluster(NgSqlConfig clusterConf) {
        
    }

    public Map<String, NgSqlNode> getNodes() {
        return nodes;
    }

    public Map<String, List<String>> getGroups() {
        return groups;
    }
    
}
