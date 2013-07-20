package org.ng12306.sql.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 12306);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		char[] cbuf = new char[1024];
		
		while (true) {
			String str = bf.readLine();
			out.println(str);
			out.flush();
			if("exit".equals(str)){
				break;
			}
			int len = in.read(cbuf);   
			if(len != -1) {
				System.out.println(cbuf);
			}
		}
		socket.close();
	}

}
