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
    byte[] SERVER_VERSION = "5.1.48-cobar-1.2.7".getBytes();
}
