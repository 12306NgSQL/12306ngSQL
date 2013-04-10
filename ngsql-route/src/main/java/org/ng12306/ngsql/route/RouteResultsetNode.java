package org.ng12306.ngsql.route;

/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-4-9 下午10:03:46
* @version: 1.0
 */
public class RouteResultsetNode {

	public final static Integer DEFAULT_REPLICA_INDEX = -1;

    private final String name; // 数据节点名称
    private final int replicaIndex;// 数据源编号
    private final String statement; // 执行的语句

    public RouteResultsetNode(String name, String statement) {
        this(name, DEFAULT_REPLICA_INDEX, statement);
    }

    public RouteResultsetNode(String name, int index, String statement) {
        this.name = name;
        this.replicaIndex = index;
        this.statement = statement;
    }

    public String getName() {
        return name;
    }

    public int getReplicaIndex() {
        return replicaIndex;
    }

    public String getStatement() {
        return statement;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof RouteResultsetNode) {
            RouteResultsetNode rrn = (RouteResultsetNode) obj;
            if (replicaIndex == rrn.getReplicaIndex() && equals(name, rrn.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(name).append('.');
        if (replicaIndex < 0) {
            s.append("default");
        } else {
            s.append(replicaIndex);
        }
        s.append('{').append(statement).append('}');
        return s.toString();
    }

    private static boolean equals(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }
        return str1.equals(str2);
    }
}
