package simplenetworking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

public class SimpleChatServer {
	public void go() {
		try {
			ServerSocket server = new ServerSocket(4242);
			while (true) {
				Socket s = server.accept();
				Thread thread = new Thread(s);
				new Thread(() -> {
					echoMessages(s);
				}).start();
			}
		}catch (Exception e){

		}
	}

	private PrintWriter	writer;					// writer 를 바깥으로 뺀 이유 생각해보기
	private void echoMessages(Socket sock) {
		new Thread(() -> {
			BufferedReader reader;
			try {
				writer = new PrintWriter(sock.getOutputStream());
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				while (true) {
					String s = reader.readLine();
					writer.println(s);
					writer.flush();
				}
			}catch (Exception ex){
			}
		}).start();

	}
}
