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

import org.ng12306.ngsql.parser.ast.expression.primary.Identifier;
import org.ng12306.ngsql.parser.ast.stmt.SQLStatement;
import org.ng12306.ngsql.parser.visitor.SQLASTVisitor;


/**
 * 
* [添加说明]
* @author: <a href="mailto:lvbomr@gmail.com">lvbo</a>
* @date: 2013-5-26 上午11:59:49
* @version: 1.0
 */
public class MTSRollbackStatement implements SQLStatement {
    public static enum CompleteType {
        /** not specified, then use default */
        UN_DEF,
        CHAIN,
        /** MySQL's default */
        NO_CHAIN,
        RELEASE,
        NO_RELEASE
    }

    private final CompleteType completeType;
    private final Identifier savepoint;

    public MTSRollbackStatement(CompleteType completeType) {
        if (completeType == null) throw new IllegalArgumentException("complete type is null!");
        this.completeType = completeType;
        this.savepoint = null;
    }

    public MTSRollbackStatement(Identifier savepoint) {
        this.completeType = null;
        if (savepoint == null) throw new IllegalArgumentException("savepoint is null!");
        this.savepoint = savepoint;
    }

    /**
     * @return null if roll back to SAVEPOINT
     */
    public CompleteType getCompleteType() {
        return completeType;
    }

    /**
     * @return null for roll back the whole transaction
     */
    public Identifier getSavepoint() {
        return savepoint;
    }

    @Override
    public void accept(SQLASTVisitor visitor) {
        visitor.visit(this);
    }

}
