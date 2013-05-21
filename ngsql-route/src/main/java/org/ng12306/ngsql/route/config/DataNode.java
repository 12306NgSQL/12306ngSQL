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
package org.ng12306.ngsql.route.config;

/*-
 * 对应rule.xml dataNode属性
 * @author:Fredric 
 * @date: 2013-5-21
 */
public class DataNode {
	private String datasource;
	private String user;
	private String password;
	private String sqlmode;
	
	public DataNode(String datasource, String user, String password, String sqlmode){
		this.datasource = datasource;
		this.user       = user;
		this.password   = password;
		this.sqlmode    = sqlmode;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSqlmode() {
		return sqlmode;
	}

	public void setSqlmode(String sqlmode) {
		this.sqlmode = sqlmode;
	}
}
