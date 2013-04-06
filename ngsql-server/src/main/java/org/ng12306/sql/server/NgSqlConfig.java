package org.ng12306.sql.server;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.ng12306.ngsql.model.DataSourceConfig;
import org.ng12306.ngsql.model.QuarantineConfig;
import org.ng12306.ngsql.model.SchemaConfig;
import org.ng12306.ngsql.model.SystemConfig;
import org.ng12306.ngsql.model.UserConfig;
import org.ng12306.ngsql.util.TimeUtil;

/**
 * 
* [处理NgSql权限注册]
* @author: lvbo
* @date: 2013-4-5 下午8:09:58
* @version: 1.0
 */
public class NgSqlConfig {

	private static final int RELOAD = 1;
    private static final int ROLLBACK = 2;

    private volatile SystemConfig system;
    private volatile NgSqlCluster cluster;
    private volatile NgSqlCluster _cluster;
    private volatile QuarantineConfig quarantine;
    private volatile QuarantineConfig _quarantine;
    private volatile Map<String, UserConfig> users;
    private volatile Map<String, UserConfig> _users;
    private volatile Map<String, SchemaConfig> schemas;
    private volatile Map<String, SchemaConfig> _schemas;
    private volatile Map<String, DataSourceConfig> dataSources;
    private volatile Map<String, DataSourceConfig> _dataSources;
    private long reloadTime;
    private long rollbackTime;
    private int status;
    private final ReentrantLock lock;

    public NgSqlConfig() {
        ConfigInitializer confInit = new ConfigInitializer();
        this.system = confInit.getSystem();
        this.users = confInit.getUsers();
        this.schemas = confInit.getSchemas();
        this.dataSources = confInit.getDataSources();
        this.quarantine = confInit.getQuarantine();
        this.cluster = confInit.getCluster();

        this.reloadTime = TimeUtil.currentTimeMillis();
        this.rollbackTime = -1L;
        this.status = RELOAD;
        this.lock = new ReentrantLock();
    }

    public SystemConfig getSystem() {
        return system;
    }

    public Map<String, UserConfig> getUsers() {
        return users;
    }

    public Map<String, UserConfig> getBackupUsers() {
        return _users;
    }

    public Map<String, SchemaConfig> getSchemas() {
        return schemas;
    }

    public Map<String, SchemaConfig> getBackupSchemas() {
        return _schemas;
    }

    public Map<String, DataSourceConfig> getDataSources() {
        return dataSources;
    }

    public Map<String, DataSourceConfig> getBackupDataSources() {
        return _dataSources;
    }

    public NgSqlCluster getCluster() {
        return cluster;
    }

    public NgSqlCluster getBackupCluster() {
        return _cluster;
    }

    public QuarantineConfig getQuarantine() {
        return quarantine;
    }

    public QuarantineConfig getBackupQuarantine() {
        return _quarantine;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public long getReloadTime() {
        return reloadTime;
    }

    public long getRollbackTime() {
        return rollbackTime;
    }

    public void reload(Map<String, UserConfig> users, Map<String, SchemaConfig> schemas,
            Map<String, DataSourceConfig> dataSources, NgSqlCluster cluster,
            QuarantineConfig quarantine) {
        apply(users, schemas, dataSources, cluster, quarantine);
        this.reloadTime = TimeUtil.currentTimeMillis();
        this.status = RELOAD;
    }

    public boolean canRollback() {
        if (_users == null || _schemas == null || _dataSources == null || _cluster == null
                || _quarantine == null || status == ROLLBACK) {
            return false;
        } else {
            return true;
        }
    }

    public void rollback(Map<String, UserConfig> users, Map<String, SchemaConfig> schemas,
            Map<String, DataSourceConfig> dataSources, NgSqlCluster cluster,
            QuarantineConfig quarantine) {
        apply(users, schemas, dataSources, cluster, quarantine);
        this.rollbackTime = TimeUtil.currentTimeMillis();
        this.status = ROLLBACK;
    }

    private void apply(Map<String, UserConfig> users, Map<String, SchemaConfig> schemas,
            Map<String, DataSourceConfig> dataSources, NgSqlCluster cluster,
            QuarantineConfig quarantine) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            // stop mysql heartbeat
            // stop cobar heartbeat
            NgSqlCluster oldCluster = this.cluster;
            if (oldCluster != null) {
            }
            this._users = this.users;
            this._schemas = this.schemas;
            this._dataSources = this.dataSources;
            this._cluster = this.cluster;
            this._quarantine = this.quarantine;

            // start mysql heartbeat
            // start cobar heartbeat
            if (cluster != null) {
            }
            this.users = users;
            this.schemas = schemas;
            this.dataSources = dataSources;
            this.cluster = cluster;
            this.quarantine = quarantine;
        } finally {
            lock.unlock();
        }
    }
}
