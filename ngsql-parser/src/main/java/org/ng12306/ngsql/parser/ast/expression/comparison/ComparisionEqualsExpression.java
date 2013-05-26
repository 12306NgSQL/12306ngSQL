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

import java.util.Map;

import org.ng12306.ngsql.parser.ast.expression.BinaryOperatorExpression;
import org.ng12306.ngsql.parser.ast.expression.Expression;
import org.ng12306.ngsql.parser.ast.expression.ReplacableExpression;
import org.ng12306.ngsql.parser.ast.expression.primary.literal.LiteralBoolean;
import org.ng12306.ngsql.parser.util.ExprEvalUtils;
import org.ng12306.ngsql.parser.util.Pair;
import org.ng12306.ngsql.parser.visitor.SQLASTVisitor;



/**
 * 
* [添加说明]
* @author: <a href="mailto:lvbomr@gmail.com">lvbo</a>
* @date: 2013-5-25 下午12:31:25
* @version: 1.0
 */
public class ComparisionEqualsExpression extends BinaryOperatorExpression implements ReplacableExpression {

    public ComparisionEqualsExpression(Expression leftOprand, Expression rightOprand) {
        super(leftOprand, rightOprand, PRECEDENCE_COMPARISION);
    }

    @Override
    public String getOperator() {
        return "=";
    }

    @Override
    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters) {
        Object left = leftOprand.evaluation(parameters);
        Object right = rightOprand.evaluation(parameters);
        if (left == null || right == null) return null;
        if (left == UNEVALUATABLE || right == UNEVALUATABLE) return UNEVALUATABLE;
        if (left instanceof Number || right instanceof Number) {
            Pair<Number, Number> pair = ExprEvalUtils.convertNum2SameLevel(left, right);
            left = pair.getKey();
            right = pair.getValue();
        }
        return left.equals(right) ? LiteralBoolean.TRUE : LiteralBoolean.FALSE;
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

	@Override
	public int getPrecedence() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Expression setCacheEvalRst(boolean cacheEvalRst) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
