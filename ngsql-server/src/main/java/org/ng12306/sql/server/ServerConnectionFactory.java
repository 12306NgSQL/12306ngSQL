/*
 * Copyright 2012-2013 NgSql Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
