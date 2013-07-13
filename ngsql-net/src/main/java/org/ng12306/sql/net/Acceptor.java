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
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ng12306.sql.net.connection.FrontendConnection;
import org.ng12306.sql.net.factory.FrontendConnectionFactory;


/**
 * 接收前端数据
 * @author lvbo
 *
 */
public class Acceptor extends Thread {
	private static final Logger LOGGER = Logger.getLogger(Acceptor.class);
	private static final AcceptIdGenerator ID_GENERATOR = new AcceptIdGenerator();
	
	private final int port;
    private final Selector selector;
    private final ServerSocketChannel serverChannel;
    private final FrontendConnectionFactory factory;
    private Processor[] processors;
    private int nextProcessor;
    
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
    
    public void setProcessors(Processor[] processors) {
        this.processors = processors;
    }
    
    @Override
    public void run() {
    	final Selector selector = this.selector;
        for (;;) {
            try {
                selector.select(1000L);
                Set<SelectionKey> keys = selector.selectedKeys();
                try {
                    for (SelectionKey key : keys) {
                        if (key.isValid() && key.isAcceptable()) {
                            accept();
                        } else {
                            key.cancel();
                        }
                    }
                } finally {
                    keys.clear();
                }
            } catch (Throwable e) {
                LOGGER.warn(getName(), e);
            }
        }
    }
    
    /**
     * 接收处理前端数据
     */
    private void accept() {
    	SocketChannel channel = null;
        try {
            channel = serverChannel.accept();
            channel.configureBlocking(false);
            FrontendConnection c = factory.make(channel);
            c.setAccepted(true);
            c.setId(ID_GENERATOR.getId());
            Processor processor = nextProcessor();
            c.setProcessor(processor);
            processor.postRegister(c);
        } catch (Throwable e) {
            closeChannel(channel);
            LOGGER.warn(getName(), e);
        }
    }
    
    private static class AcceptIdGenerator {

        private static final long MAX_VALUE = 0xffffffffL;

        private long acceptId = 0L;
        private final Object lock = new Object();

        private long getId() {
            synchronized (lock) {
                if (acceptId >= MAX_VALUE) {
                    acceptId = 0L;
                }
                return ++acceptId;
            }
        }
    }
    
    private static void closeChannel(SocketChannel channel) {
        if (channel == null) {
            return;
        }
        Socket socket = channel.socket();
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
        try {
            channel.close();
        } catch (IOException e) {
        }
    }
    
    private Processor nextProcessor() {
        if (++nextProcessor == processors.length) {
            nextProcessor = 0;
        }
        return processors[nextProcessor];
    }
}
