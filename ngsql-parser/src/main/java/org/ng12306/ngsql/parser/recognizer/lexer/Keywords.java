package org.ng12306.ngsql.parser.recognizer.lexer;

import java.util.HashMap;
import java.util.Map;

import org.ng12306.ngsql.parser.recognizer.Token;

/**
 * @author <a href="mailto:wenjie.0617@gmail.com">wuwj</a>
 */
public class Keywords {
    public static final Keywords DEFAULT_KEYWORDS = new Keywords();

    private final Map<String, Token> keywords = new HashMap<String, Token>(230);

    private Keywords() {
        for (Token type : Token.class.getEnumConstants()) {
            String name = type.name();
            if (name.startsWith("KW_")) {
                String kw = name.substring("KW_".length());
                keywords.put(kw, type);
            }
        }
        keywords.put("NULL", Token.LITERAL_NULL);
        keywords.put("FALSE", Token.LITERAL_BOOL_FALSE);
        keywords.put("TRUE", Token.LITERAL_BOOL_TRUE);
    }

    /**
     * @param keyUpperCase must be uppercase
     * @return <code>KeyWord</code> or {@link Token#LITERAL_NULL NULL} or
     *         {@link Token#LITERAL_BOOL_FALSE FALSE} or
     *         {@link Token#LITERAL_BOOL_TRUE TRUE}
     */
    public Token getKeyword(String keyUpperCase) {
        return keywords.get(keyUpperCase);
    }

}
