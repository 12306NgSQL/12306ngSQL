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

import org.ng12306.ngsql.parser.ast.expression.primary.Identifier;
import org.ng12306.ngsql.parser.ast.expression.primary.literal.LiteralString;
import org.ng12306.ngsql.parser.visitor.SQLASTVisitor;


/**
 * 
* [添加说明]
* @author: <a href="mailto:lvbomr@gmail.com">lvbo</a>
* @date: 2013-5-26 上午11:33:14
* @version: 1.0
 */
public  class AliasableTableReference implements TableReference {
    protected final String alias;
    protected String aliasUpEscape;

    public AliasableTableReference(String alias) {
        this.alias = alias;
    }

    /**
     * @return upper-case, empty is possible
     */
    public String getAliasUnescapeUppercase() {
        if (alias == null || alias.length() <= 0) return alias;
        if (aliasUpEscape != null) return aliasUpEscape;

        switch (alias.charAt(0)) {
        case '`':
            return aliasUpEscape = Identifier.unescapeName(alias, true);
        case '\'':
            return aliasUpEscape = LiteralString.getUnescapedString(alias.substring(1, alias.length() - 1), true);
        case '_':
            int ind = -1;
            for (int i = 1; i < alias.length(); ++i) {
                if (alias.charAt(i) == '\'') {
                    ind = i;
                    break;
                }
            }
            if (ind >= 0) {
                LiteralString st =
                        new LiteralString(alias.substring(0, ind), alias.substring(ind + 1, alias.length() - 1), false);
                return aliasUpEscape = st.getUnescapedString(true);
            }
        default:
            return aliasUpEscape = alias.toUpperCase();
        }
    }

    public String getAlias() {
        return alias;
    }

	@Override
	public void accept(SQLASTVisitor visitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object removeLastConditionElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSingleTable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getPrecedence() {
		// TODO Auto-generated method stub
		return 0;
	}
}
