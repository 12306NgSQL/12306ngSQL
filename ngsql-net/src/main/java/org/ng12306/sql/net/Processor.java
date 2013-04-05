package org.ng12306.sql.net;

import java.io.IOException;

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
}
