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
package org.ng12306.sql.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import org.ng12306.sql.net.Processor;
import org.ng12306.sql.net.connection.FrontendConnectionFactory;


/**
 * 接收前端数据
 * @author lvbo
 *
 */
public class Acceptor extends Thread {

	private final int port;
    private final Selector selector;
    private final ServerSocketChannel serverChannel;
    private final FrontendConnectionFactory factory;
    private Processor[] processors;
    
    public Acceptor(String name, int port, FrontendConnectionFactory factory) throws IOException {
        super.setName(name);
        this.port = port;
        this.selector = Selector.open();
        this.serverChannel = ServerSocketChannel.open();
        this.serverChannel.socket().bind(new InetSocketAddress(port));
        this.serverChannel.configureBlocking(false);
        this.serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        this.factory = factory;
    }
    
    @Override
    public void run() {
    	
    }
    
    /**
     * 接收处理前端数据
     */
    private void accept() {
    	
    }
}
