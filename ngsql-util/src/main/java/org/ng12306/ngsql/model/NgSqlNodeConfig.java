package org.ng12306.ngsql.model;

/**
 * 
* [节点注册操作]
* @author: lvbo
* @date: 2013-4-5 下午8:30:59
* @version: 1.0
 */
public class NgSqlNodeConfig {

	private String name;
    private String host;
    private int port;
    private int weight;

    public NgSqlNodeConfig(String name, String host, int port, int weight) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("[name=").append(name).append(",host=").append(host).append(",port=")
                .append(port).append(",weight=").append(weight).append(']').toString();
    }
}
