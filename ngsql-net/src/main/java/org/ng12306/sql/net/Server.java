package net;

import java.io.IOException;
import java.util.TimerTask;

/**
 * 服务端
 * @author lvbo
 *
 */
public class Server {

	private static final Server INSTANCE = new Server();
	
	private NIOProcessor[] processors;
    private NIOConnector connector;
    private NIOAcceptor server;
	
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

	public NIOProcessor[] getProcessors() {
		return processors;
	}

	public void setProcessors(NIOProcessor[] processors) {
		this.processors = processors;
	}

	public NIOConnector getConnector() {
		return connector;
	}

	public void setConnector(NIOConnector connector) {
		this.connector = connector;
	}

	public NIOAcceptor getServer() {
		return server;
	}

	public void setServer(NIOAcceptor server) {
		this.server = server;
	}
	
	/**
	 * 数据节点定时连接空闲超时检查任务
	 * @return
	 */
	private TimerTask dataNodeIdleCheck() {}
	
	/**
	 * 定时心跳检查
	 * @return
	 */
	private TimerTask dataNodeHeartbeat() {}
	
	
}
