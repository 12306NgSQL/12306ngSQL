package org.ng12306.ngsql.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-4-9 下午9:30:43
* @version: 1.0
 */
public class NameableExecutor extends ThreadPoolExecutor {

	protected String name;

    public NameableExecutor(String name, int size, BlockingQueue<Runnable> queue, ThreadFactory factory) {
        super(size, size, Long.MAX_VALUE, TimeUnit.NANOSECONDS, queue, factory);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
