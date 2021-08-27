package net.omny.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.moandjiezana.toml.Toml;

import lombok.Getter;
import net.omny.route.Request;
import net.omny.route.Router;
import net.omny.utils.ConfigFile;
import net.omny.utils.Debug;
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

		webServer.init();
		
		webServer.threadPool.submit(() -> {
			// Run the server
			try (ServerSocket serverSocket = new ServerSocket(webServer.port)) {
				Debug.debug("Listening on port "+webServer.port);
				webServer.running.set(true);
				while (webServer.running.get()) {
					Socket client = serverSocket.accept();
					webServer.threadPool.submit(
						() -> {
							Debug.debug(Thread.currentThread().getName()+" is handling "+client.getInetAddress());
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
	protected int port = (int) ConfigFile.DEFAULT_PORT;
	@Getter 
	private final AtomicBoolean running = new AtomicBoolean(false);
	
	public WebServer(String configFile) {
		this();
		this.router = new Router();
		//TODO Load config file
		Toml toml = new Toml().read(new File(configFile));
		this.port = toml.getLong(ConfigFile.PORT, ConfigFile.DEFAULT_PORT).intValue();
		
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
		route(this.router);
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
	 * The handler must close the socket itself due to Multithreading
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param clientSocket
	 * @throws IOException 
	 * @date 08/08/2021
	 */
	public void handler(Socket clientSocket) throws IOException {
		Debug.reset("handle_request");
		BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		StringBuilder requestBuilder = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null && !line.isBlank()) {
			// Following the RFC at this link: https://datatracker.ietf.org/doc/html/rfc2616
			// Line breaking is describe as CR LF => \r \n
			requestBuilder.append(line + "\r\n");
		}
		Request request = Request.parse(requestBuilder.toString());
		
		this.router.handleRoute(request, clientSocket);
		
		
		clientSocket.close();
		Debug.time("handle_request", request.getMethod()+" on '"+request.getPath()+"' processed in {ms} ms.");
	}
	
}
