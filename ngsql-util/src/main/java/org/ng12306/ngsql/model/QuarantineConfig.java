package org.ng12306.ngsql.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
* [隔离区配置定义]
* @author: lvbo
* @date: 2013-4-5 下午8:44:06
* @version: 1.0
 */
public class QuarantineConfig {

	private final Map<String, Set<String>> hosts;

    public QuarantineConfig() {
        hosts = new HashMap<String, Set<String>>();
    }

    public Map<String, Set<String>> getHosts() {
        return hosts;
    }
}
