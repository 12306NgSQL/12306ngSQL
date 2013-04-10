package org.ng12306.sql.server;

import java.nio.channels.SocketChannel;

import org.ng12306.ngsql.model.SystemConfig;
import org.ng12306.sql.net.ServerConnection;
import org.ng12306.sql.net.connection.FrontendConnection;
import org.ng12306.sql.net.factory.FrontendConnectionFactory;
import org.ng12306.sql.net.session.BlockingSession;

/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-4-7 下午9:18:34
* @version: 1.0
 */
public class ServerConnectionFactory extends FrontendConnectionFactory {

	@Override
    protected FrontendConnection getConnection(SocketChannel channel) {
        SystemConfig sys = Server.getInstance().getConfig().getSystem();
        ServerConnection c = new ServerConnection(channel);
        c.setPrivileges(new NgSqlPrivileges());
       // c.setQueryHandler(new QueryHandler(c));
        // c.setPrepareHandler(new ServerPrepareHandler(c)); TODO prepare
        c.setTxIsolation(sys.getTxIsolation());
        c.setSession(new BlockingSession(c));
      // c.setSession2(new NonBlockingSession(c));
        return c;
    }
}
