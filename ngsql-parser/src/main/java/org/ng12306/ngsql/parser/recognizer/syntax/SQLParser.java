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
