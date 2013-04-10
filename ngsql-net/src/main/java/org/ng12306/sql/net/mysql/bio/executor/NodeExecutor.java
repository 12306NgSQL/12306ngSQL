package org.ng12306.sql.net.mysql.bio.executor;

/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-4-10 上午6:44:28
* @version: 1.0
 */
public abstract class NodeExecutor {

	/**
     * @return block until all tasks are finished
     */
    public abstract void terminate() throws InterruptedException;
}
