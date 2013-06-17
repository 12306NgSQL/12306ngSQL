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

import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;

import org.ng12306.ngsql.parser.visitor.SQLASTVisitor;


/**
 * 
* [添加说明]
* @author: <a href="mailto:lvbomr@gmail.com">lvbo</a>
* @date: 2013-5-26 上午11:36:12
* @version: 1.0
 */
public class TableReferences implements TableReference {
    protected static List<TableReference> ensureListType(List<TableReference> list) {
        if (list instanceof ArrayList) return list;
        return new ArrayList<TableReference>(list);
    }

    private final List<TableReference> list;

    /**
     * @return never null
     */
    public List<TableReference> getTableReferenceList() {
        return list;
    }

    public TableReferences(List<TableReference> list) throws SQLSyntaxErrorException {
        if (list == null || list.isEmpty()) {
            throw new SQLSyntaxErrorException("at least one table reference");
        }
        this.list = ensureListType(list);
    }

    @Override
    public Object removeLastConditionElement() {
        if (list != null && !list.isEmpty()) {
            return list.get(list.size() - 1).removeLastConditionElement();
        }
        return null;
    }

    @Override
    public boolean isSingleTable() {
        if (list == null) {
            return false;
        }
        int count = 0;
        TableReference first = null;
        for (TableReference ref : list) {
            if (ref != null && 1 == ++count) {
                first = ref;
            }
        }
        return count == 1 && first.isSingleTable();
    }

    @Override
    public int getPrecedence() {
        return TableReference.PRECEDENCE_REFS;
    }

    @Override
    public void accept(SQLASTVisitor visitor) {
        visitor.visit(this);
    }

}
