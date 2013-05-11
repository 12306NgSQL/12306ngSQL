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
package org.ng12306.ngsql.route;

import org.ng12306.ngsql.route.config.SchemaConfig;

/*-
 * @author:Fredric
 * @date: 2013-5-10
 */
public final class ServerRouter {
	
	public static RouteResultset route(SchemaConfig config, String stmt){
		
		RouteResultset rrs = new RouteResultset(stmt);
		
		//匹配规则
		
		//路由处理
				
		return rrs;
	}
}
