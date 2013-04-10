package org.ng12306.sql.net.mysql;

/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-4-9 下午9:15:00
* @version: 1.0
 */
public class QuitPacket extends MySQLPacket{

	public static final byte[] QUIT = new byte[] { 1, 0, 0, 0, 1 };

    @Override
    public int calcPacketSize() {
        return 1;
    }

    @Override
    protected String getPacketInfo() {
        return "MySQL Quit Packet";
    }
}
