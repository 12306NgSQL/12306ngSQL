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

import static org.ng12306.ngsql.parser.recognizer.Token.EOF;
import static org.ng12306.ngsql.parser.recognizer.Token.KW_RELEASE;

import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.Map;

import org.ng12306.ngsql.parser.ast.expression.primary.Identifier;
import org.ng12306.ngsql.parser.ast.stmt.mts.MTSReleaseStatement;
import org.ng12306.ngsql.parser.ast.stmt.mts.MTSRollbackStatement;
import org.ng12306.ngsql.parser.ast.stmt.mts.MTSSavepointStatement;
import org.ng12306.ngsql.parser.recognizer.lexer.SQLLexer;



/**
 * 
* [添加说明]
* @author: <a href="mailto:lvbomr@gmail.com">lvbo</a>
* @date: 2013-5-26 下午12:05:10
* @version: 1.0
 */
public class MySQLMTSParser extends MySQLParser {
    private static enum SpecialIdentifier {
        CHAIN,
        NO,
        RELEASE,
        SAVEPOINT,
        WORK
    }

    private static final Map<String, SpecialIdentifier> specialIdentifiers = new HashMap<String, SpecialIdentifier>();
    static {
        specialIdentifiers.put("SAVEPOINT", SpecialIdentifier.SAVEPOINT);
        specialIdentifiers.put("WORK", SpecialIdentifier.WORK);
        specialIdentifiers.put("CHAIN", SpecialIdentifier.CHAIN);
        specialIdentifiers.put("RELEASE", SpecialIdentifier.RELEASE);
        specialIdentifiers.put("NO", SpecialIdentifier.NO);
    }

    public MySQLMTSParser(SQLLexer lexer) {
        super(lexer);
    }

    /**
     * first token <code>SAVEPOINT</code> is scanned but not yet consumed
     */
    public MTSSavepointStatement savepoint() throws SQLSyntaxErrorException {
        // matchIdentifier("SAVEPOINT"); // for performance issue, change to follow:
        lexer.nextToken();
        Identifier id = identifier();
        match(EOF);
        return new MTSSavepointStatement(id);
    }

    /**
     * first token <code>RELEASE</code> is scanned but not yet consumed
     */
    public MTSReleaseStatement release() throws SQLSyntaxErrorException {
        match(KW_RELEASE);
        matchIdentifier("SAVEPOINT");
        Identifier id = identifier();
        match(EOF);
        return new MTSReleaseStatement(id);
    }

    /**
     * first token <code>ROLLBACK</code> is scanned but not yet consumed
     * 
     * <pre>
     *         ROLLBACK [WORK] TO [SAVEPOINT] identifier
     *         ROLLBACK [WORK] [AND [NO] CHAIN | [NO] RELEASE]
     * </pre>
     */
    public MTSRollbackStatement rollback() throws SQLSyntaxErrorException {
        //matchIdentifier("ROLLBACK"); // for performance issue, change to follow:
        lexer.nextToken();
        SpecialIdentifier siTemp = specialIdentifiers.get(lexer.stringValueUppercase());
        if (siTemp == SpecialIdentifier.WORK) {
            lexer.nextToken();
        }
        switch (lexer.token()) {
        case EOF:
            return new MTSRollbackStatement(MTSRollbackStatement.CompleteType.UN_DEF);
        case KW_TO:
            lexer.nextToken();
            String str = lexer.stringValueUppercase();
            if (specialIdentifiers.get(str) == SpecialIdentifier.SAVEPOINT) {
                lexer.nextToken();
            }
            Identifier savepoint = identifier();
            match(EOF);
            return new MTSRollbackStatement(savepoint);
        case KW_AND:
            lexer.nextToken();
            siTemp = specialIdentifiers.get(lexer.stringValueUppercase());
            if (siTemp == SpecialIdentifier.NO) {
                lexer.nextToken();
                matchIdentifier("CHAIN");
                match(EOF);
                return new MTSRollbackStatement(MTSRollbackStatement.CompleteType.NO_CHAIN);
            }
            matchIdentifier("CHAIN");
            match(EOF);
            return new MTSRollbackStatement(MTSRollbackStatement.CompleteType.CHAIN);
        case KW_RELEASE:
            lexer.nextToken();
            match(EOF);
            return new MTSRollbackStatement(MTSRollbackStatement.CompleteType.RELEASE);
        case IDENTIFIER:
            siTemp = specialIdentifiers.get(lexer.stringValueUppercase());
            if (siTemp == SpecialIdentifier.NO) {
                lexer.nextToken();
                match(KW_RELEASE);
                match(EOF);
                return new MTSRollbackStatement(MTSRollbackStatement.CompleteType.NO_RELEASE);
            }
        default:
            throw err("unrecognized complete type: " + lexer.token());
        }
    }

}
