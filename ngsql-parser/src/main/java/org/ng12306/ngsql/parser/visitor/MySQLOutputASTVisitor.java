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
package org.ng12306.ngsql.parser.visitor;

/**
 * @author <a href="mailto:wenjie.0617@gmail.com">wuwj</a>
 */
public final class MySQLOutputASTVisitor implements SQLASTVisitor {

    private static final Object[] EMPTY_OBJ_ARRAY = new Object[0];
    private static final int[] EMPTY_INT_ARRAY = new int[0];
    private final StringBuilder appendable;
    private final Object[] args;
    private int[] argsIndex;

    public MySQLOutputASTVisitor(StringBuilder appendable) {
        this(appendable, null);
    }

    /**
     * @param args parameters for {@link java.sql.PreparedStatement
     *            preparedStmt}
     */
    public MySQLOutputASTVisitor(StringBuilder appendable, Object[] args) {
        this.appendable = appendable;
        this.args = args == null ? EMPTY_OBJ_ARRAY : args;
        this.argsIndex = args == null ? EMPTY_INT_ARRAY : new int[args.length];
    }

}
