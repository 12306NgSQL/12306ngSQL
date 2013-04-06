package org.ng12306.sql.net.handler;

import java.util.Set;

/**
 * 
* [权限提供者]
* @author: lvbo
* @date: 2013-4-5 下午8:05:42
* @version: 1.0
 */
public interface Privileges {

	/**
     * 检查schema是否存在
     */
    boolean schemaExists(String schema);

    /**
     * 检查用户是否存在，并且可以使用host实行隔离策略。
     */
    boolean userExists(String user, String host);

    /**
     * 提供用户的服务器端密码
     */
    String getPassword(String user);

    /**
     * 提供有效的用户schema集合
     */
    Set<String> getUserSchemas(String user);
}
