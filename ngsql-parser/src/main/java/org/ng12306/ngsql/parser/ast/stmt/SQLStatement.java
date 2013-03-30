package org.ng12306.ngsql.parser.ast.stmt;

import org.ng12306.ngsql.parser.ast.ASTNode;

/**
 * @author <a href="mailto:wenjie.0617@gmail.com">wuwj</a>
 */
public interface SQLStatement extends ASTNode {
    public static enum StmtType {
        DML_SELECT,
        DML_DELETE,
        DML_INSERT,
        DML_REPLACE,
        DML_UPDATE,
        DML_CALL,
        DAL_SET,
        DAL_SHOW,
        MTL_START,
        /** COMMIT or ROLLBACK */
        MTL_TERMINATE,
        MTL_ISOLATION
    }
}
