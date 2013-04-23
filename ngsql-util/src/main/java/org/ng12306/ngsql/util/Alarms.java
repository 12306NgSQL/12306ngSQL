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
