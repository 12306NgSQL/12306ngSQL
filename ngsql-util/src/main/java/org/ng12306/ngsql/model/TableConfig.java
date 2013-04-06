package org.ng12306.ngsql.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ng12306.ngsql.util.SplitUtil;

/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-4-5 下午9:01:55
* @version: 1.0
 */
public class TableConfig {

	private final String name;
    private final String[] dataNodes;
    private final TableRuleConfig rule;
    private final Set<String> columnIndex;
    private final boolean ruleRequired;

    public TableConfig(String name, String dataNode, TableRuleConfig rule, boolean ruleRequired) {
        if (name == null) {
            throw new IllegalArgumentException("table name is null");
        }
        this.name = name.toUpperCase();
        this.dataNodes = SplitUtil.split(dataNode, ',', '$', '-', '[', ']');
        if (this.dataNodes == null || this.dataNodes.length <= 0) {
            throw new IllegalArgumentException("invalid table dataNodes: " + dataNode);
        }
        this.rule = rule;
        this.columnIndex = buildColumnIndex(rule);
        this.ruleRequired = ruleRequired;
    }

    public boolean existsColumn(String columnNameUp) {
        return columnIndex.contains(columnNameUp);
    }

    /**
     * @return upper-case
     */
    public String getName() {
        return name;
    }

    public String[] getDataNodes() {
        return dataNodes;
    }

    public boolean isRuleRequired() {
        return ruleRequired;
    }

    public TableRuleConfig getRule() {
        return rule;
    }

    private static Set<String> buildColumnIndex(TableRuleConfig rule) {
        if (rule == null) {
            return Collections.emptySet();
        }
        List<RuleConfig> rs = rule.getRules();
        if (rs == null || rs.isEmpty()) {
            return Collections.emptySet();
        }
        Set<String> columnIndex = new HashSet<String>();
        for (RuleConfig r : rs) {
            List<String> columns = r.getColumns();
            if (columns != null) {
                for (String col : columns) {
                    if (col != null) {
                        columnIndex.add(col.toUpperCase());
                    }
                }
            }
        }
        return columnIndex;
    }
}
