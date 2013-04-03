package org.ng12306.sql.net.connection;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;

/**
 * 数据连接操作接口
 * @author lvbo
 *
 */
public interface Connection {

	/**
	 * 注册网络事件
	 * @param selector
	 * @throws IOException
	 */
    void register(Selector selector) throws IOException;

    /**
     * 读取数据
     * @throws IOException
     */
    void read() throws IOException;

    /**
     * 处理数据
     * @param data
     */
    void handle(byte[] data);

    /**
     * 写出一块缓存数据
     * @param buffer
     */
    void write(ByteBuffer buffer);

    /**
     * 基于处理器队列的方式写数据
     * @throws IOException
     */
    void writeByQueue() throws IOException;

    /**
     * 基于监听事件的方式写数据
     * @throws IOException
     */
    void writeByEvent() throws IOException;

    /**
     * 发生错误
     * @param errCode
     * @param t
     */
    void error(int errCode, Throwable t);

    /**
     * 关闭连接
     * @return
     */
    boolean close();
}
