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
