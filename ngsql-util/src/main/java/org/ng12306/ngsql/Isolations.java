package org.ng12306.ngsql;

/**
 * 
* [事务隔离级别定义]
* @author: lvbo
* @date: 2013-4-5 下午8:21:56
* @version: 1.0
 */
public interface Isolations {

	int READ_UNCOMMITTED = 1;
    int READ_COMMITTED = 2;
    int REPEATED_READ = 3;
    int SERIALIZABLE = 4;
}
