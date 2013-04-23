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
package org.ng12306.ngsql.parser.recognizer.syntax;

import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.Map;

import org.ng12306.ngsql.parser.recognizer.lexer.SQLLexer;

/**
 * @author <a href="mailto:wenjie.0617@gmail.com">wuwj</a>
 */
public class SQLParser {
    public static final String DEFAULT_CHARSET = "utf-8";
    protected final SQLLexer lexer;

    public SQLParser(SQLLexer lexer) {
        this(lexer, true);
    }

    public SQLParser(SQLLexer lexer, boolean cacheEvalRst) {
        this.lexer = lexer;
        this.cacheEvalRst = cacheEvalRst;
    }

    private static enum SpecialIdentifier {
        GLOBAL,
        LOCAL,
        SESSION
    }

    private static final Map<String, SpecialIdentifier> specialIdentifiers = new HashMap<String, SpecialIdentifier>();
    static {
        specialIdentifiers.put("GLOBAL", SpecialIdentifier.GLOBAL);
        specialIdentifiers.put("SESSION", SpecialIdentifier.SESSION);
        specialIdentifiers.put("LOCAL", SpecialIdentifier.LOCAL);
    }

    protected final boolean cacheEvalRst;

    /**
     * side-effect is forbidden
     */
    protected SQLSyntaxErrorException err(String msg) throws SQLSyntaxErrorException {
        StringBuilder errmsg = new StringBuilder();
        errmsg.append(msg).append(". lexer state: ").append(String.valueOf(lexer));
        throw new SQLSyntaxErrorException(errmsg.toString());
    }
}
