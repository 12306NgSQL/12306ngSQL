package org.ng12306.sql.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NgSqlServerTest implements Runnable {
	
	private static final byte[] FIRST_BYTES = "Welcome to NgSql Server. \n".getBytes();
	
	private final ServerSocket serverSocket;
	
	private NgSqlServerTest(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	@Override
	public void run() {
		while (true) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(new BioConnection(socket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	private class BioConnection implements Runnable {

        private Socket socket;
        private InputStream input;
        private OutputStream output;
        private byte[] readBuffer;
        private byte[] writeBuffer;

        private BioConnection(Socket socket) throws IOException {
            this.socket = socket;
            this.input = socket.getInputStream();
            this.output = socket.getOutputStream();
            this.readBuffer = new byte[4096];
            this.writeBuffer = new byte[4096];
        }

        @Override
        public void run() {
            try {
            	System.out.println("接收到客户端请求");
                output.write(FIRST_BYTES);
                output.flush();
                while (true) {
                    int got = input.read(readBuffer);
                    if(got == -1) continue;
                    output.write(writeBuffer, 0, got);
                    output.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (socket != null) try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		new Thread(new NgSqlServerTest(12306)).start();
	}

}
