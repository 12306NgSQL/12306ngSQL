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
>>>>>>> nothing
