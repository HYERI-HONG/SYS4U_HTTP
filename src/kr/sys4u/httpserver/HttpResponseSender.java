package kr.sys4u.httpserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import kr.sys4u.httpserver.exception.NoSuchMIMETypeException;

public class HttpResponseSender {

	private final static String WEB_ROOT = "C:/Users/notebiz7750/Documents/test";
	// private final static String WEB_ROOT = "C:/Users/hyeri/Documents/test";

	private final Socket socket;
	private final CacheManager cacheManager;

	public HttpResponseSender(Socket socket, CacheManager cacheManager) {

		this.socket = socket;
		this.cacheManager = cacheManager;
	}

	public void send(StatusLineParser parser) {

		parser.parse();
		
		if (parser.getExtension().equals("ico")) {
			return;
		}

		File file = new File(WEB_ROOT + parser.getFileName());
		
		try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);) {

			String mimeType = new MIMETypeManager().getMIMEType(parser.getExtension());
			String response = String.format(
					"HTTP/1.1 200 OK\r\n" 
							+ "Content-Type: %s; charset=UTF-8\r\n" 
							+ "Content-Length: %d\r\n\r\n",
							mimeType, file.length());

			writer.write(response);
			writer.flush();

			new FileSender(socket, cacheManager).send(file);

		} catch (FileNotFoundException e) {

		} catch (IOException e1) {

		}

	}

}
