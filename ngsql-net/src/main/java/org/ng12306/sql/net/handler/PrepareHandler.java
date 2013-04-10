package org.ng12306.sql.net.handler;

/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-4-9 下午9:06:19
* @version: 1.0
 */
public interface PrepareHandler {

	void prepare(String sql);

    void execute(byte[] data);

    void close();
}
