package org.ng12306.sql.server;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.helpers.LogLog;


public class NgSqlServerStartup {

	private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";

    public static void main(String[] args) {
        try {
            // init
        	ServerSocketChannel serverChannel = ServerSocketChannel.open();
        	serverChannel.socket().bind(new InetSocketAddress(9066));
        	Server server = Server.getInstance();
            server.beforeStart(dateFormat);

            //startup
            //server.startup();
        } catch (Throwable e) {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            LogLog.error(sdf.format(new Date()) + " startup error", e);
            System.exit(-1);
        }
    }
}
