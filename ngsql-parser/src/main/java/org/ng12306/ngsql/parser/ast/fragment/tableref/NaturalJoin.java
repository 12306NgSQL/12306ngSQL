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
package org.ng12306.ngsql.parser.ast.fragment.tableref;

import org.ng12306.ngsql.parser.visitor.SQLASTVisitor;


/**
 * 
* [添加说明]
* @author: <a href="mailto:lvbomr@gmail.com">lvbo</a>
* @date: 2013-5-26 上午11:34:41
* @version: 1.0
 */
public class NaturalJoin implements TableReference {
    private final boolean isOuter;
    /**
     * make sense only if {@link #isOuter} is true. Eigher <code>LEFT</code> or
     * <code>RIGHT</code>
     */
    private final boolean isLeft;
    private final TableReference leftTableRef;
    private final TableReference rightTableRef;

    public NaturalJoin(boolean isOuter, boolean isLeft, TableReference leftTableRef, TableReference rightTableRef) {
        super();
        this.isOuter = isOuter;
        this.isLeft = isLeft;
        this.leftTableRef = leftTableRef;
        this.rightTableRef = rightTableRef;
    }

    public boolean isOuter() {
        return isOuter;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public TableReference getLeftTableRef() {
        return leftTableRef;
    }

    public TableReference getRightTableRef() {
        return rightTableRef;
    }

    @Override
    public Object removeLastConditionElement() {
        return null;
    }

    @Override
    public boolean isSingleTable() {
        return false;
    }

    @Override
    public int getPrecedence() {
        return TableReference.PRECEDENCE_JOIN;
    }

    @Override
    public void accept(SQLASTVisitor visitor) {
        visitor.visit(this);
    }
}
