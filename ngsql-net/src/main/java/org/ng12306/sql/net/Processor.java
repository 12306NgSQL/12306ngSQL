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
import java.util.concurrent.ConcurrentMap;

import org.ng12306.ngsql.statistic.CommandCount;
import org.ng12306.ngsql.util.NameableExecutor;
import org.ng12306.sql.net.buffer.BufferPool;
import org.ng12306.sql.net.connection.BackendConnection;
import org.ng12306.sql.net.connection.Connection;
import org.ng12306.sql.net.connection.FrontendConnection;

/**
 * 处理前端请求、释放资源等操作
 * @author lvbo
 *
 */
public class Processor {

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024 * 16;
    private static final int DEFAULT_BUFFER_CHUNK_SIZE = 4096;
    private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
    
	private final Reactor reactor;
	private BufferPool bufferPool;
	private long netInBytes;
	private long netOutBytes;
	
	private  ConcurrentMap<Long, FrontendConnection> frontends;
    private  ConcurrentMap<Long, BackendConnection> backends;
    
    private  NameableExecutor handler;
    private  CommandCount commands;
	
	public Processor(String name) throws IOException {
		
		this(name, DEFAULT_BUFFER_SIZE, DEFAULT_BUFFER_CHUNK_SIZE, AVAILABLE_PROCESSORS, AVAILABLE_PROCESSORS);
    }

    public Processor(String name, int handler, int executor) throws IOException {
    	this(name, DEFAULT_BUFFER_SIZE, DEFAULT_BUFFER_CHUNK_SIZE, handler, executor);
    }

    public Processor(String name, int buffer, int chunk, int handler, int executor) throws IOException {
    	
    	this.reactor = new Reactor(name);
    }
    
    public void startup() {
        reactor.startup();
    }
    
    public BufferPool getBufferPool() {
        return bufferPool;
    }
    
    public void addNetInBytes(long bytes) {
        netInBytes += bytes;
    }
    
    public void postWrite(Connection c) {
        reactor.postWrite(c);
    }
    
    public void addNetOutBytes(long bytes) {
        netOutBytes += bytes;
    }
    
    public void addBackend(BackendConnection c) {
        backends.put(c.getId(), c);
    }
    
    public void addFrontend(FrontendConnection c) {
        frontends.put(c.getId(), c);
    }
    
    public NameableExecutor getHandler() {
        return handler;
    }
    
    public CommandCount getCommands() {
        return commands;
    }
}
