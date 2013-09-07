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
package org.ng12306.ngsql.model;

/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-4-9 下午9:44:59
* @version: 1.0
 */
public interface Versions {

	/** 协议版本 */
    byte PROTOCOL_VERSION = 10;

    /** 服务器版本 */
    byte[] SERVER_VERSION = "5.1.48-ngsql-1.2.7".getBytes();
}
