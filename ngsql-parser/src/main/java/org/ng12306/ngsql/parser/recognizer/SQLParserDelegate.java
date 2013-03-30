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
