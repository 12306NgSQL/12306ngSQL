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
package org.ng12306.ngsql.parser.ast.expression.arithmeic;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import org.ng12306.ngsql.parser.ast.expression.Expression;
import org.ng12306.ngsql.parser.ast.expression.UnaryOperatorExpression;
import org.ng12306.ngsql.parser.util.ExprEvalUtils;
import org.ng12306.ngsql.parser.util.UnaryOperandCalculator;
import org.ng12306.ngsql.parser.visitor.SQLASTVisitor;



/**
 * 
* [添加说明]
* @author: <a href="mailto:lvbomr@gmail.com">lvbo</a>
* @date: 2013-5-25 下午12:27:20
* @version: 1.0
 */
public class MinusExpression extends UnaryOperatorExpression implements UnaryOperandCalculator {
    public MinusExpression(Expression operand) {
        super(operand, PRECEDENCE_UNARY_OP);
    }

    @Override
    public String getOperator() {
        return "-";
    }

    @Override
    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters) {
        Object operand = getOperand().evaluation(parameters);
        if (operand == null) return null;
        if (operand == UNEVALUATABLE) return UNEVALUATABLE;
        Number num = null;
        if (operand instanceof String) {
            num = ExprEvalUtils.string2Number((String) operand);
        } else {
            num = (Number) operand;
        }
        return ExprEvalUtils.calculate(this, num);
    }

    @Override
    public Number calculate(Integer num) {
        if (num == null) return null;
        int n = num.intValue();
        if (n == Integer.MIN_VALUE) {
            return new Long(-(long) n);
        }
        return new Integer(-n);
    }

    @Override
    public Number calculate(Long num) {
        if (num == null) return null;
        long n = num.longValue();
        if (n == Long.MIN_VALUE) {
            return new Long(-(long) n);
        }
        return new Long(-n);
    }

    @Override
    public Number calculate(BigInteger num) {
        if (num == null) return null;
        return num.negate();
    }

    @Override
    public Number calculate(BigDecimal num) {
        if (num == null) return null;
        return num.negate();
    }

	@Override
	public void accept(SQLASTVisitor visitor) {
		// TODO Auto-generated method stub
		
	}
}
