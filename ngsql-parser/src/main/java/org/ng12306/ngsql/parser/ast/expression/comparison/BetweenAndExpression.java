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
package org.ng12306.ngsql.parser.ast.expression.comparison;

import org.ng12306.ngsql.parser.ast.expression.Expression;
import org.ng12306.ngsql.parser.ast.expression.ReplacableExpression;
import org.ng12306.ngsql.parser.ast.expression.TernaryOperatorExpression;
import org.ng12306.ngsql.parser.visitor.SQLASTVisitor;



/**
 * 
* [添加说明]
* @author: <a href="mailto:lvbomr@gmail.com">lvbo</a>
* @date: 2013-5-25 下午12:19:11
* @version: 1.0
 */
public class BetweenAndExpression extends TernaryOperatorExpression implements ReplacableExpression {
    private final boolean not;

    public BetweenAndExpression(boolean not, Expression comparee, Expression notLessThan, Expression notGreaterThan) {
        super(comparee, notLessThan, notGreaterThan);
        this.not = not;
    }

    public boolean isNot() {
        return not;
    }

    @Override
    public int getPrecedence() {
        return PRECEDENCE_BETWEEN_AND;
    }

    private Expression replaceExpr;

    @Override
    public void setReplaceExpr(Expression replaceExpr) {
        this.replaceExpr = replaceExpr;
    }

    @Override
    public void clearReplaceExpr() {
        this.replaceExpr = null;
    }

    @Override
    public void accept(SQLASTVisitor visitor) {
        if (replaceExpr == null) visitor.visit(this);
        else replaceExpr.accept(visitor);
    }
}
