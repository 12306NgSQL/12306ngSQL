package org.ng12306.sql.server;

import java.io.IOException;

import org.ng12306.sql.net.Acceptor;
import org.ng12306.sql.net.Connector;
import org.ng12306.sql.net.Processor;

/**
 * 服务端
 * @author lvbo
 *
 */
public class Server {

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
	public void startup() throws IOException {}

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
