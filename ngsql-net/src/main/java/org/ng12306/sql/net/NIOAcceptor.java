package net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import net.connection.FrontendConnectionFactory;

/**
 * 接收前端数据
 * @author lvbo
 *
 */
public class NIOAcceptor extends Thread {

	private final int port;
    private final Selector selector;
    private final ServerSocketChannel serverChannel;
    private final FrontendConnectionFactory factory;
    private NIOProcessor[] processors;
    
    public NIOAcceptor(String name, int port, FrontendConnectionFactory factory) throws IOException {
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
