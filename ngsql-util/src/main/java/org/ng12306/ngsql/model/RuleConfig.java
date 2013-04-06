package org.ng12306.ngsql.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-4-5 下午9:04:12
* @version: 1.0
 */
public class RuleConfig {

	private final List<String> columns;
    private final String algorithm;
    private RuleAlgorithm ruleAlgorithm;

    public RuleConfig(String[] columns, String algorithm) {
        if (algorithm == null) {
            throw new IllegalArgumentException("algorithm is null");
        }
        this.algorithm = algorithm;
        if (columns == null || columns.length <= 0) {
            throw new IllegalArgumentException("no rule column is found");
        }
        List<String> list = new ArrayList<String>(columns.length);
        for (String column : columns) {
            if (column == null) {
                throw new IllegalArgumentException("column value is null: " + columns);
            }
            list.add(column.toUpperCase());
        }
        this.columns = Collections.unmodifiableList(list);
    }

    public RuleAlgorithm getRuleAlgorithm() {
        return ruleAlgorithm;
    }

    public void setRuleAlgorithm(RuleAlgorithm ruleAlgorithm) {
        this.ruleAlgorithm = ruleAlgorithm;
    }

    /**
     * @return unmodifiable, upper-case
     */
    public List<String> getColumns() {
        return columns;
    }

    /**
     * @return never null
     */
    public String getAlgorithm() {
        return algorithm;
    }
}
