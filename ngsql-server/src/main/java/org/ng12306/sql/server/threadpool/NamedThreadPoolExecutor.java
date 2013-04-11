package org.ng12306.sql.server.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NamedThreadPoolExecutor extends ThreadPoolExecutor {
	private String name; //线程池名称
	
	/***
	 * 
	 * @param name 
	 * @param size  线程池中所保存的线程数 和 池中允许的最大线程数 
	 * @param queue 执行前用于保持任务的队列。此队列仅保持由 execute 方法提交的 Runnable 任务。
	 * @param factory 执行程序创建新线程时使用的工厂
	 */
	public NamedThreadPoolExecutor(String name, int size, BlockingQueue<Runnable> queue, ThreadFactory factory){
		super(size, size, Long.MAX_VALUE, TimeUnit.NANOSECONDS, queue, factory);
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	
}
