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
package org.ng12306.sql.server.threadpool;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/***
 *  创建线程池的工厂
 * @author sean
 *
 */
public class NamedThreadPoolFactory {
	
	public static NamedThreadPoolExecutor createThreadPool(String name, int size){
		return createThreadPool(name,size,true);
	}
	
	/***
	 *  创建线程池
	 * @param name
	 * @param size
	 * @param idDaemon
	 * @return
	 */
	public static NamedThreadPoolExecutor createThreadPool(String name, int size, boolean idDaemon){
		NamedThreadFactory factory = new NamedThreadFactory(name,idDaemon);
        return new NamedThreadPoolExecutor(name, size, new LinkedBlockingQueue<Runnable>(), factory);
	}
	
	
	static class NamedThreadFactory implements ThreadFactory{
		private final ThreadGroup group;   //线程组
        private final String namePrefix;  //
        private final AtomicInteger threadId;  
        private final boolean isDaemon; 

        public NamedThreadFactory(String name, boolean isDaemon){
        	SecurityManager s = System.getSecurityManager();
            this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = name;   
            this.threadId = new AtomicInteger(0);  
            this.isDaemon = isDaemon;
        }
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + threadId.getAndIncrement());
            t.setDaemon(isDaemon);
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
		}
		
	}

}
