package org.ng12306.sql.server;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.ng12306.ngsql.model.SystemConfig;
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
    private final NgSqlConfig config;
	
	public static final Server getInstance() {
        return INSTANCE;
    }
	
	private Server() {
		this.config = new NgSqlConfig();
		SystemConfig system = config.getSystem();
	}
	
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
        SystemConfig system = config.getSystem();
        // startup processors
        LOGGER.info("Startup processors ...");
        processors = new Processor[4];
        int handler = 2;
        int executor = 4;
        for (int i = 0; i < processors.length; i++) {
            processors[i] = new Processor("Processor" + i, handler, executor);
            processors[i].startup();
        }
        
        // startup connector
        LOGGER.info("Startup connector ...");
        connector = new Connector(NAME + "Connector");
        connector.setProcessors(processors);
        connector.start();
        
        // startup server
        ServerConnectionFactory sf = new ServerConnectionFactory();
        sf.setCharset(system.getCharset());
        sf.setIdleTimeout(system.getIdleTimeout());
        server = new Acceptor(NAME + "Server", system.getServerPort(), sf);
        server.setProcessors(processors);
        server.start();
        
        // server started
        LOGGER.info(server.getName() + " is started and listening on " + server.getPort());
        LOGGER.info("===============================================");
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
	
	public NgSqlConfig getConfig() {
        return config;
    }
}
