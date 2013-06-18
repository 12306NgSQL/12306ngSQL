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
package org.ng12306.ngsql.parser.ast.stmt.mts;

import org.ng12306.ngsql.parser.ast.fragment.VariableScope;
import org.ng12306.ngsql.parser.ast.stmt.SQLStatement;
import org.ng12306.ngsql.parser.visitor.SQLASTVisitor;


/**
 * 
* [添加说明]
* @author: <a href="mailto:lvbomr@gmail.com">lvbo</a>
* @date: 2013-5-26 下午12:00:32
* @version: 1.0
 */
public class MTSSetTransactionStatement implements SQLStatement {
    public static enum IsolationLevel {
        READ_UNCOMMITTED,
        READ_COMMITTED,
        REPEATABLE_READ,
        SERIALIZABLE
    }

    private final VariableScope scope;
    private final IsolationLevel level;

    public MTSSetTransactionStatement(VariableScope scope, IsolationLevel level) {
        super();
        if (level == null) throw new IllegalArgumentException("isolation level is null");
        this.level = level;
        this.scope = scope;
    }

    /**
     * @retern null means scope undefined
     */
    public VariableScope getScope() {
        return scope;
    }

    public IsolationLevel getLevel() {
        return level;
    }

    @Override
    public void accept(SQLASTVisitor visitor) {
        visitor.visit(this);
    }
}
