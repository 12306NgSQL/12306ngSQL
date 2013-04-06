package org.ng12306.ngsql.util;

/**
 * 
* [NgSql报警关键词定义]
* @author: lvbo
* @date: 2013-4-6 下午3:39:04
* @version: 1.0
 */
public interface Alarms {

	/** 默认报警关键词 **/
    String DEFAULT = "#!NgSql#";

    /** 集群无有效的节点可提供服务 **/
    String CLUSTER_EMPTY = "#!CLUSTER_EMPTY#";

    /** 数据节点的数据源发生切换 **/
    String DATANODE_SWITCH = "#!DN_SWITCH#";

    /** 隔离区非法用户访问 **/
    String QUARANTINE_ATTACK = "#!QT_ATTACK#";
}
