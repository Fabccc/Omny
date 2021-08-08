package net.omny.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Getter;
import net.omny.route.Request;
import net.omny.route.Router;
import net.omny.utils.Ex;

public abstract class WebServer {

	/**
	 * Create a webserver builder
	 * @author Fabien CAYRE (Computer)
	 *
	 * @return WebServerBuilder the builder to create the web server
	 * @date 08/08/2021
	 */
	public static WebServerBuilder builder() {
		return new WebServerBuilder();
	}
	
	/**
	 * Launch the provided instance 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param webServer The WebServer instance
	 * @date 08/08/2021
	 */
	public static void launch(WebServer webServer) {
		if(webServer.threadPool == null) {
			// Thread pool was not provided
			// 4 default
			//TODO replace this value with either value from configFile or value depending on system capabilities
			webServer.threadPool = Executors.newScheduledThreadPool(4);
		}
		
		webServer.threadPool.submit(() -> {
			// Run the server
			try (ServerSocket serverSocket = new ServerSocket(webServer.port)) {
				System.out.println("Listening on port "+webServer.port);
				webServer.running.set(true);
				while (webServer.running.get()) {
					Socket client = serverSocket.accept();
					webServer.threadPool.submit(
						() -> {
							System.out.println(Thread.currentThread().getName()+" is handling "+client.getInetAddress());
							Ex.grab(() -> webServer.handler(client));
						});
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		});
		
	}
	
	private Router router;
	private WebServerConfig config;
	private ExecutorService threadPool;
	@Getter
	protected int port = 8080;
	@Getter 
	private final AtomicBoolean running = new AtomicBoolean(false);
	
	public WebServer(String configFile) {
		this();
		//TODO Load config file
	}
	
	public WebServer(String configFile, ExecutorService threadPool) {
		this(configFile);
		this.threadPool = threadPool;
	}
	
	public WebServer() {}
	
	private void init() {
		//TODO init the web server
		// -> handling routes
		// -> FUTURE : handling middleware
	}
	
	/**
	 * Initializing routes for the webserver
	 * 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param router The main router of the application
	 * @date 08/08/2021
	 */
	public abstract void route(Router router);
	
	/**
	 * The handler of clientSocket
	 * The handler must close the socket itmself due to Multithreading
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param clientSocket
	 * @throws IOException 
	 * @date 08/08/2021
	 */
	public void handler(Socket clientSocket) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		StringBuilder requestBuilder = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null && !line.isBlank()) {
			// Following the RFC at this link: https://datatracker.ietf.org/doc/html/rfc2616
			// Line breaking is describe as CR LF => \r \n
			requestBuilder.append(line + "\r\n");
		}
		
		Request.parse(requestBuilder.toString());
		
		clientSocket.close();
	}
	
}
