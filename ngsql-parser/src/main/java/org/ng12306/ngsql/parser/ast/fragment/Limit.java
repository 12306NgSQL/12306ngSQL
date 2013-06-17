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
package org.ng12306.ngsql.parser.ast.fragment;

import org.ng12306.ngsql.parser.ast.ASTNode;
import org.ng12306.ngsql.parser.ast.expression.primary.ParamMarker;
import org.ng12306.ngsql.parser.visitor.SQLASTVisitor;


/**
 * 
* [添加说明]
* @author: <a href="mailto:lvbomr@gmail.com">lvbo</a>
* @date: 2013-5-26 上午11:37:13
* @version: 1.0
 */
public class Limit implements ASTNode {
    /** when it is null, to sql generated must ignore this number */
    private final Number offset;
    private final Number size;
    private final ParamMarker offsetP;
    private final ParamMarker sizeP;

    public Limit(Number offset, Number size) {
        if (offset == null) throw new IllegalArgumentException();
        if (size == null) throw new IllegalArgumentException();
        this.offset = offset;
        this.size = size;
        this.offsetP = null;
        this.sizeP = null;
    }

    public Limit(Number offset, ParamMarker sizeP) {
        if (offset == null) throw new IllegalArgumentException();
        if (sizeP == null) throw new IllegalArgumentException();
        this.offset = offset;
        this.size = null;
        this.offsetP = null;
        this.sizeP = sizeP;
    }

    public Limit(ParamMarker offsetP, Number size) {
        if (offsetP == null) throw new IllegalArgumentException();
        if (size == null) throw new IllegalArgumentException();
        this.offset = null;
        this.size = size;
        this.offsetP = offsetP;
        this.sizeP = null;
    }

    public Limit(ParamMarker offsetP, ParamMarker sizeP) {
        if (offsetP == null) throw new IllegalArgumentException();
        if (sizeP == null) throw new IllegalArgumentException();
        this.offset = null;
        this.size = null;
        this.offsetP = offsetP;
        this.sizeP = sizeP;
    }

    /**
     * @return {@link ParamMarker} or {@link Number}
     */
    public Object getOffset() {
        return offset == null ? offsetP : offset;
    }

    /**
     * @return {@link ParamMarker} or {@link Number}
     */
    public Object getSize() {
        return size == null ? sizeP : size;
    }

    @Override
    public void accept(SQLASTVisitor visitor) {
        visitor.visit(this);
    }
}
