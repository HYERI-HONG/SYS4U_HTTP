package kr.sys4u.httpserver;

import java.io.IOException;
import java.net.Socket;

public class RunnableHttpServer implements Runnable {

	private final Socket socket;
	private final CacheManager cacheManager;

	public RunnableHttpServer(Socket socket, CacheManager cacheManager) {
		this.socket = socket;
		this.cacheManager = cacheManager;
	}

	@Override
	public void run() {

		try {
			String statusLine = new HttpRequestReceiver(socket).getStatusLine();
			new HttpResponseSender(socket,cacheManager).send(new StatusLineParser(statusLine));	
			socket.close();
			
		} catch (IOException e) {
			
		}
	}
}
