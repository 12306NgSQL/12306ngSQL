package org.ng12306.ngsql.util;

/**
 * 
* [弱精度的计时器，考虑性能不使用同步策略]
* @author: lvbo
* @date: 2013-4-6 下午3:24:19
* @version: 1.0
 */
public class TimeUtil {

	private static long CURRENT_TIME = System.currentTimeMillis();

    public static final long currentTimeMillis() {
        return CURRENT_TIME;
    }

    public static final void update() {
        CURRENT_TIME = System.currentTimeMillis();
    }
}
