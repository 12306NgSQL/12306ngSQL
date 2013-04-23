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

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ng12306.ngsql.model.UserConfig;
import org.ng12306.ngsql.util.Alarms;
import org.ng12306.sql.net.handler.Privileges;

/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-4-5 下午8:07:46
* @version: 1.0
 */
public class NgSqlPrivileges implements Privileges {

	private static final Logger ALARM = Logger.getLogger("alarm");

    @Override
    public boolean schemaExists(String schema) {
    	NgSqlConfig conf = Server.getInstance().getConfig();
        return conf.getSchemas().containsKey(schema);
    }

    @Override
    public boolean userExists(String user, String host) {
        NgSqlConfig conf = Server.getInstance().getConfig();
        Map<String, Set<String>> quarantineHosts = conf.getQuarantine().getHosts();
        if (quarantineHosts.containsKey(host)) {
            boolean rs = quarantineHosts.get(host).contains(user);
            if (!rs) {
                ALARM.error(new StringBuilder().append(Alarms.QUARANTINE_ATTACK).append("[host=").append(host)
                        .append(",user=").append(user).append(']').toString());
            }
            return rs;
        } else {
            if (user != null && user.equals(conf.getSystem().getClusterHeartbeatUser())) {
                return true;
            } else {
                return conf.getUsers().containsKey(user);
            }
        }
    }

    @Override
    public String getPassword(String user) {
        NgSqlConfig conf = Server.getInstance().getConfig();
        if (user != null && user.equals(conf.getSystem().getClusterHeartbeatUser())) {
            return conf.getSystem().getClusterHeartbeatPass();
        } else {
            UserConfig uc = conf.getUsers().get(user);
            if (uc != null) {
                return uc.getPassword();
            } else {
                return null;
            }
        }
    }

    @Override
    public Set<String> getUserSchemas(String user) {
    	NgSqlConfig conf = Server.getInstance().getConfig();
        UserConfig uc = conf.getUsers().get(user);
        if (uc != null) {
            return uc.getSchemas();
        } else {
            return null;
        }
    }
}
