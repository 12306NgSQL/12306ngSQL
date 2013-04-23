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
package org.ng12306.sql.server;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.ng12306.sql.net.Acceptor;
import org.ng12306.sql.net.Connector;
import org.ng12306.sql.net.Processor;

/**
 * 服务端
 * @author lvbo
 *
 */
public class Server {
	
	public static final String NAME = "NgSql";
	private static final Logger LOGGER = Logger.getLogger(Server.class);

	private static final Server INSTANCE = new Server();
	
	private Processor[] processors;
    private Connector connector;
    private Acceptor server;
	
	public static final Server getInstance() {
        return INSTANCE;
    }
	
	private Server() {}
	
	/**
	 * 处理一些预加载内容
	 * @param dateFormat
	 */
	public void beforeStart(String dateFormat) {}
	
	/**
	 * 启动服务
	 * @throws IOException
	 */
	public void startup() throws IOException {
		// server startup
        LOGGER.info("===============================================");
        LOGGER.info(NAME + " is ready to startup ...");
        
        // startup processors
        LOGGER.info("Startup processors ...");
        processors = new Processor[4];
        int handler = 2;
        int executor = 4;
        for (int i = 0; i < processors.length; i++) {
            processors[i] = new Processor("Processor" + i, handler, executor);
            processors[i].startup();
        }
	}

	public Processor[] getProcessors() {
		return processors;
	}

	public void setProcessors(Processor[] processors) {
		this.processors = processors;
	}

	public Connector getConnector() {
		return connector;
	}

	public void setConnector(Connector connector) {
		this.connector = connector;
	}

	public Acceptor getServer() {
		return server;
	}

	public void setServer(Acceptor server) {
		this.server = server;
	}
	
	
}
