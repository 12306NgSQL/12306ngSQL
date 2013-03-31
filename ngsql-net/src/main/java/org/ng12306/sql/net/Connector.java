package org.ng12306.sql.net;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.ng12306.sql.net.connection.Connection;


/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-3-31 下午9:09:13
* @version: 1.0
 */
public final class Connector extends Thread {
    private static final Logger LOGGER = Logger.getLogger(Connector.class);
    private static final ConnectIdGenerator ID_GENERATOR = new ConnectIdGenerator();

    private final String name;
    private final Selector selector;
    private final BlockingQueue<Connection> connectQueue;
    private Processor[] processors;
    private int nextProcessor;
    private long connectCount;

    public Connector(String name) throws IOException {
        super.setName(name);
        this.name = name;
        this.selector = Selector.open();
        this.connectQueue = new LinkedBlockingQueue<Connection>();
    }

    public long getConnectCount() {
        return connectCount;
    }

    public void setProcessors(Processor[] processors) {
        this.processors = processors;
    }

    public void postConnect(Connection c) {
        //connectQueue.offer(c);
        selector.wakeup();
    }

    @Override
    public void run() {
        final Selector selector = this.selector;
        for (;;) {
            ++connectCount;
            try {
                selector.select(1000L);
                connect(selector);
                Set<SelectionKey> keys = selector.selectedKeys();
                try {
                    for (SelectionKey key : keys) {
                        Object att = key.attachment();
                        if (att != null && key.isValid() && key.isConnectable()) {
                            finishConnect(key, att);
                        } else {
                            key.cancel();
                        }
                    }
                } finally {
                    keys.clear();
                }
            } catch (Throwable e) {
                LOGGER.warn(name, e);
            }
        }
    }

    private void connect(Selector selector) {
        Connection c = null;
//        while ((c = connectQueue.poll()) != null) {
//            try {
//                c.connect(selector);
//            } catch (Throwable e) {
//                c.error(ErrorCode.ERR_CONNECT_SOCKET, e);
//            }
//        }
    }

    private void finishConnect(SelectionKey key, Object att) {
        Connection c = (Connection) att;
        try {
//            if (c.finishConnect()) {
//                clearSelectionKey(key);
//                c.setId(ID_GENERATOR.getId());
//                Processor processor = nextProcessor();
//                c.setProcessor(processor);
//                processor.postRegister(c);
//            }
        } catch (Throwable e) {
            clearSelectionKey(key);
        }
    }

    private void clearSelectionKey(SelectionKey key) {
        if (key.isValid()) {
            key.attach(null);
            key.cancel();
        }
    }

    private Processor nextProcessor() {
        if (++nextProcessor == processors.length) {
            nextProcessor = 0;
        }
        return processors[nextProcessor];
    }

    /**
     * 后端连接ID生成器
     * 
     * @author xianmao.hexm
     */
    private static class ConnectIdGenerator {

        private static final long MAX_VALUE = Long.MAX_VALUE;

        private long connectId = 0L;
        private final Object lock = new Object();

        private long getId() {
            synchronized (lock) {
                if (connectId >= MAX_VALUE) {
                    connectId = 0L;
                }
                return ++connectId;
            }
        }
    }

}
