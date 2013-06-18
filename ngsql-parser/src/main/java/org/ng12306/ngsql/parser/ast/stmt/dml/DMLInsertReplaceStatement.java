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
package org.ng12306.ngsql.parser.ast.stmt.dml;

import java.util.List;

import org.ng12306.ngsql.parser.ast.expression.misc.QueryExpression;
import org.ng12306.ngsql.parser.ast.expression.primary.Identifier;
import org.ng12306.ngsql.parser.ast.expression.primary.RowExpression;


/**
 * 
* [添加说明]
* @author: <a href="mailto:lvbomr@gmail.com">lvbo</a>
* @date: 2013-5-26 上午11:55:54
* @version: 1.0
 */
public abstract class DMLInsertReplaceStatement extends DMLStatement {
    protected final Identifier table;
    protected final List<Identifier> columnNameList;
    protected List<RowExpression> rowList;
    protected final QueryExpression select;

    @SuppressWarnings("unchecked")
    public DMLInsertReplaceStatement(Identifier table, List<Identifier> columnNameList, List<RowExpression> rowList) {
        this.table = table;
        this.columnNameList = ensureListType(columnNameList);
        this.rowList = ensureListType(rowList);
        this.select = null;
    }

    @SuppressWarnings("unchecked")
    public DMLInsertReplaceStatement(Identifier table, List<Identifier> columnNameList, QueryExpression select) {
        if (select == null) throw new IllegalArgumentException("argument 'select' is empty");
        this.select = select;
        this.table = table;
        this.columnNameList = ensureListType(columnNameList);
        this.rowList = null;
    }

    public Identifier getTable() {
        return table;
    }

    /**
     * @return {@link java.util.ArrayList ArrayList}
     */
    public List<Identifier> getColumnNameList() {
        return columnNameList;
    }

    /**
     * @return {@link java.util.ArrayList ArrayList} or
     *         {@link java.util.Collections#emptyList() EMPTY_LIST}
     */
    public List<RowExpression> getRowList() {
        return rowList;
    }

    public QueryExpression getSelect() {
        return select;
    }

    private List<RowExpression> rowListBak;

    public void setReplaceRowList(List<RowExpression> list) {
        rowListBak = rowList;
        rowList = list;
    }

    public void clearReplaceRowList() {
        if (rowListBak != null) {
            rowList = rowListBak;
            rowListBak = null;
        }
    }

}
