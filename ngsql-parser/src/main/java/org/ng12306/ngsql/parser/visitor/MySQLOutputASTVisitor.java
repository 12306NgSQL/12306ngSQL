package org.ng12306.ngsql.parser.visitor;

/**
 * @author <a href="mailto:wenjie.0617@gmail.com">wuwj</a>
 */
public final class MySQLOutputASTVisitor implements SQLASTVisitor {

    private static final Object[] EMPTY_OBJ_ARRAY = new Object[0];
    private static final int[] EMPTY_INT_ARRAY = new int[0];
    private final StringBuilder appendable;
    private final Object[] args;
    private int[] argsIndex;

    public MySQLOutputASTVisitor(StringBuilder appendable) {
        this(appendable, null);
    }

    /**
     * @param args parameters for {@link java.sql.PreparedStatement
     *            preparedStmt}
     */
    public MySQLOutputASTVisitor(StringBuilder appendable, Object[] args) {
        this.appendable = appendable;
        this.args = args == null ? EMPTY_OBJ_ARRAY : args;
        this.argsIndex = args == null ? EMPTY_INT_ARRAY : new int[args.length];
    }

}
