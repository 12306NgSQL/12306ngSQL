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
