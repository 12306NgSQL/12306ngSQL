package org.ng12306.ngsql.model;

import java.util.Set;

/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-4-5 下午8:58:25
* @version: 1.0
 */
public class UserConfig {

	private String name;
    private String password;
    private Set<String> schemas;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getSchemas() {
        return schemas;
    }

    public void setSchemas(Set<String> schemas) {
        this.schemas = schemas;
    }
}
