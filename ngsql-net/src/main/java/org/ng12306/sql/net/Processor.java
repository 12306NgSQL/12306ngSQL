package org.ng12306.sql.net;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.ng12306.ngsql.statistic.CommandCount;
import org.ng12306.ngsql.util.ExecutorUtil;
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
    
    private final String name;
	private final Reactor reactor;
	private BufferPool bufferPool;
	private long netInBytes;
    private long netOutBytes;
    private final NameableExecutor handler;
    private final NameableExecutor executor;
    private final CommandCount commands;
    
    private final ConcurrentMap<Long, FrontendConnection> frontends;
    private final ConcurrentMap<Long, BackendConnection> backends;
	
	public Processor(String name) throws IOException {
		
		this(name, DEFAULT_BUFFER_SIZE, DEFAULT_BUFFER_CHUNK_SIZE, AVAILABLE_PROCESSORS, AVAILABLE_PROCESSORS);
    }

	public Processor(String name, int handler, int executor) throws IOException {
        this(name, DEFAULT_BUFFER_SIZE, DEFAULT_BUFFER_CHUNK_SIZE, handler, executor);
    }

    public Processor(String name, int buffer, int chunk, int handler, int executor) throws IOException {
        this.name = name;
        this.reactor = new Reactor(name);
        this.bufferPool = new BufferPool(buffer, chunk);
        this.handler = (handler > 0) ? ExecutorUtil.create(name + "-H", handler) : null;
        this.executor = (executor > 0) ? ExecutorUtil.create(name + "-E", executor) : null;
        this.frontends = new ConcurrentHashMap<Long, FrontendConnection>();
        this.backends = new ConcurrentHashMap<Long, BackendConnection>();
        this.commands = new CommandCount();
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
