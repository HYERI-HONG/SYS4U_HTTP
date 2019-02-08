package kr.sys4u.httpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import kr.sys4u.httpserver.exception.CanNotConnectToClientException;

public class MainHttpServer {

	private final static int PORT = 1989;

	private static Logger logger = (Logger) LoggerFactory.getLogger(MainHttpServer.class);

	private boolean initialized;
	private ServerSocket serverSocket;
	private final ExecutorService executorService;
	private CacheManager cacheManager;

	public MainHttpServer() {
		this.initialized = false;
		this.executorService = Executors.newFixedThreadPool(10);
		this.cacheManager = new CacheManager();
	}

	public void initilize() throws IOException {

		if (initialized) {
			return;
		}

		this.serverSocket = new ServerSocket(PORT);
		this.initialized = true;
	}

	public void execute() throws IOException {

		if (!initialized) {
			initilize();
		}

		RunnableCacheChecker cacheChecker = new RunnableCacheChecker(cacheManager);
		executorService.execute(cacheChecker);

		while (true) {
			try {
				RunnableHttpServer thread = new RunnableHttpServer(serverSocket.accept(), cacheManager);
				executorService.execute(thread);
				logger.trace("trace");
				logger.debug("debug");
				logger.info("info");
				logger.warn("warn");
				logger.error("error");

			} catch (IOException e) {
				throw new CanNotConnectToClientException();
			}
			if (serverSocket.isClosed()) {
				close();
			}
		}
	}

	public void close() throws IOException {

		if (!initialized) {
			initilize();
		}

		serverSocket.close();
	}

	public static void main(String[] args) throws IOException {

		MainHttpServer server = new MainHttpServer();
		server.initilize();
		server.execute();

	}
}
