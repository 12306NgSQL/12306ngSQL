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
package org.ng12306.ngsql.parser.ast.expression.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.ng12306.ngsql.parser.ast.expression.AbstractExpression;
import org.ng12306.ngsql.parser.ast.expression.Expression;
import org.ng12306.ngsql.parser.visitor.SQLASTVisitor;



/**
 * 
* [添加说明]
* @author: <a href="mailto:lvbomr@gmail.com">lvbo</a>
* @date: 2013-5-25 下午12:36:57
* @version: 1.0
 */
public class InExpressionList extends AbstractExpression {
    private List<Expression> list;

    public InExpressionList(List<Expression> list) {
        if (list == null || list.size() == 0) {
            this.list = Collections.emptyList();
        } else if (list instanceof ArrayList) {
            this.list = list;
        } else {
            this.list = new ArrayList<Expression>(list);
        }
    }

    /**
     * @return never null
     */
    public List<Expression> getList() {
        return list;
    }

    @Override
    public int getPrecedence() {
        return PRECEDENCE_PRIMARY;
    }

    @Override
    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters) {
        return UNEVALUATABLE;
    }

    private List<Expression> replaceList;

    public void setReplaceExpr(List<Expression> replaceList) {
        this.replaceList = replaceList;
    }

    public void clearReplaceExpr() {
        this.replaceList = null;
    }

    @Override
    public void accept(SQLASTVisitor visitor) {
        if (replaceList == null) {
            visitor.visit(this);
        } else {
            List<Expression> temp = list;
            list = replaceList;
            visitor.visit(this);
            list = temp;
        }
    }
}
