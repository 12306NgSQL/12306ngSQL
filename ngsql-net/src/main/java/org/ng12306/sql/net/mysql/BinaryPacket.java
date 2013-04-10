package org.ng12306.sql.net.mysql;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.ng12306.sql.net.connection.FrontendConnection;

/**
 * 
* [添加说明]
* @author: lvbo
* @date: 2013-4-9 下午9:39:03
* @version: 1.0
 */
public class BinaryPacket extends MySQLPacket {

	public static final byte OK = 1;
    public static final byte ERROR = 2;
    public static final byte HEADER = 3;
    public static final byte FIELD = 4;
    public static final byte FIELD_EOF = 5;
    public static final byte ROW = 6;
    public static final byte PACKET_EOF = 7;

    public byte[] data;

    public void read(InputStream in) throws IOException {
        packetLength = StreamUtil.readUB3(in);
        packetId = StreamUtil.read(in);
        byte[] ab = new byte[packetLength];
        StreamUtil.read(in, ab, 0, ab.length);
        data = ab;
    }

    @Override
    public ByteBuffer write(ByteBuffer buffer, FrontendConnection c) {
        buffer = c.checkWriteBuffer(buffer, c.getPacketHeaderSize());
        BufferUtil.writeUB3(buffer, calcPacketSize());
        buffer.put(packetId);
        buffer = c.writeToBuffer(data, buffer);
        return buffer;
    }

    @Override
    public int calcPacketSize() {
        return data == null ? 0 : data.length;
    }

    @Override
    protected String getPacketInfo() {
        return "MySQL Binary Packet";
    }

}
