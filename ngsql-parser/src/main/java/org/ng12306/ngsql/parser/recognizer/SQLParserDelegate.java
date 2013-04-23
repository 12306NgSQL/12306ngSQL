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
package org.ng12306.ngsql.parser.recognizer;

import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.Map;

import org.ng12306.ngsql.parser.ast.stmt.SQLStatement;
import org.ng12306.ngsql.parser.recognizer.lexer.SQLLexer;
import org.ng12306.ngsql.parser.recognizer.syntax.SQLParser;

/**
 * @author <a href="mailto:wenjie.0617@gmail.com">wuwj</a>
 */
public final class SQLParserDelegate {

    private static enum SpecialIdentifier {
        ROLLBACK,
        SAVEPOINT,
        TRUNCATE
    }

    private static final Map<String, SpecialIdentifier> specialIdentifiers = new HashMap<String, SpecialIdentifier>();
    static {
        specialIdentifiers.put("TRUNCATE", SpecialIdentifier.TRUNCATE);
        specialIdentifiers.put("SAVEPOINT", SpecialIdentifier.SAVEPOINT);
        specialIdentifiers.put("ROLLBACK", SpecialIdentifier.ROLLBACK);
    }

    public static SQLStatement parse(SQLLexer lexer, String charset) throws SQLSyntaxErrorException {
        SQLStatement stmt = null;
        //TO-DO 编写具体的解析实现
        return stmt;
    }

    public static SQLStatement parse(String sql, String charset) throws SQLSyntaxErrorException {
        return parse(new SQLLexer(sql), charset);
    }

    public static SQLStatement parse(String sql) throws SQLSyntaxErrorException {
        return parse(sql, SQLParser.DEFAULT_CHARSET);
    }
}
