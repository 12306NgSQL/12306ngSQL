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
