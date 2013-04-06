package org.ng12306.ngsql.util;

/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-4-6 下午3:22:21
* @version: 1.0
 */
public class ConfigException extends RuntimeException {

	private static final long serialVersionUID = -180146385688342818L;

    public ConfigException() {
        super();
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }
}
