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
package org.ng12306.ngsql.parser.ast.expression;

import java.util.Map;

/**
 * 
* [sql解析]
* @author: lvbo
* @date: 2013-5-22 上午5:54:24
* @version: 1.0
 */
public  class BinaryOperatorExpression extends AbstractExpression {
    protected Expression leftOprand;
    protected Expression rightOprand;
    protected int precedence;
    protected boolean leftCombine;
    
    public BinaryOperatorExpression() {}

    /**
     * {@link #leftCombine} is true
     */
    protected BinaryOperatorExpression(Expression leftOprand, Expression rightOprand, int precedence) {
        this.leftOprand = leftOprand;
        this.rightOprand = rightOprand;
        this.precedence = precedence;
        this.leftCombine = true;
    }

    protected BinaryOperatorExpression(Expression leftOprand, Expression rightOprand, int precedence,
                                       boolean leftCombine) {
        this.leftOprand = leftOprand;
        this.rightOprand = rightOprand;
        this.precedence = precedence;
        this.leftCombine = leftCombine;
    }

    public Expression getLeftOprand() {
        return leftOprand;
    }

    public Expression getRightOprand() {
        return rightOprand;
    }

    @Override
    public int getPrecedence() {
        return precedence;
    }

    public boolean isLeftCombine() {
        return leftCombine;
    }

    public  String getOperator(){
    	return new String();
    }

    @Override
    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters) {
        return UNEVALUATABLE;
    }

}
