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
package org.ng12306.ngsql.parser.ast.expression.primary.literal;

import org.ng12306.ngsql.parser.visitor.SQLASTVisitor;


/**
 * 
* [添加说明]
* @author: <a href="mailto:lvbomr@gmail.com">lvbo</a>
* @date: 2013-5-26 上午7:40:57
* @version: 1.0
 */
public class LiteralBitField extends Literal {
    private final String text;
    private final String introducer;

    /**
     * @param introducer e.g. "_latin1"
     * @param bitFieldText e.g. "01010"
     */
    public LiteralBitField(String introducer, String bitFieldText) {
        super();
        if (bitFieldText == null) throw new IllegalArgumentException("bit text is null");
        this.introducer = introducer;
        this.text = bitFieldText;
    }

    public String getText() {
        return text;
    }

    public String getIntroducer() {
        return introducer;
    }

    @Override
    public void accept(SQLASTVisitor visitor) {
        visitor.visit(this);
    }
}
