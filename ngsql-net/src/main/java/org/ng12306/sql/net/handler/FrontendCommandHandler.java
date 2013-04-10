package org.ng12306.sql.net.handler;

import org.ng12306.ngsql.statistic.CommandCount;
import org.ng12306.ngsql.util.ErrorCode;
import org.ng12306.sql.net.Handler;
import org.ng12306.sql.net.connection.FrontendConnection;
import org.ng12306.sql.net.mysql.MySQLPacket;

/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-4-9 下午9:52:17
* @version: 1.0
 */
public class FrontendCommandHandler implements Handler {

	protected final FrontendConnection source;
    protected final CommandCount commands;

    public FrontendCommandHandler(FrontendConnection source) {
        this.source = source;
        this.commands = source.getProcessor().getCommands();
    }

    @Override
    public void handle(byte[] data) {
        switch (data[4]) {
        case MySQLPacket.COM_INIT_DB:
            commands.doInitDB();
            source.initDB(data);
            break;
        case MySQLPacket.COM_QUERY:
            commands.doQuery();
            source.query(data);
            break;
        case MySQLPacket.COM_PING:
            commands.doPing();
            source.ping();
            break;
        case MySQLPacket.COM_QUIT:
            commands.doQuit();
            source.close();
            break;
        case MySQLPacket.COM_PROCESS_KILL:
            commands.doKill();
            source.kill(data);
            break;
        case MySQLPacket.COM_STMT_PREPARE:
            commands.doStmtPrepare();
            source.stmtPrepare(data);
            break;
        case MySQLPacket.COM_STMT_EXECUTE:
            commands.doStmtExecute();
            source.stmtExecute(data);
            break;
        case MySQLPacket.COM_STMT_CLOSE:
            commands.doStmtClose();
            source.stmtClose(data);
            break;
        case MySQLPacket.COM_HEARTBEAT:
            commands.doHeartbeat();
            source.heartbeat(data);
            break;
        default:
            commands.doOther();
            source.writeErrMessage(ErrorCode.ER_UNKNOWN_COM_ERROR, "Unknown command");
        }
    }


}
