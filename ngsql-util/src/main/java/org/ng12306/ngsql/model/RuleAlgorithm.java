package org.ng12306.ngsql.model;

import java.util.Map;

/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-4-5 下午9:07:08
* @version: 1.0
 */
public interface RuleAlgorithm {

	RuleAlgorithm constructMe(Object... objects);

    void initialize();

    /**
     * @return never null
     */
    Integer[] calculate(Map<? extends Object, ? extends Object> parameters);
}
