package org.ng12306.ngsql.parser.ast;

import org.ng12306.ngsql.parser.visitor.SQLASTVisitor;

/**
 * @author <a href="mailto:wenjie.0617@gmail.com">wuwj</a>
 */
public interface ASTNode {
    void accept(SQLASTVisitor visitor);
}
