package net.omny.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import com.moandjiezana.toml.Toml;

import lombok.Getter;
import net.omny.cache.CachingRequest;
import net.omny.exceptions.MalformedRequestException;
import net.omny.route.Request;
import net.omny.route.Router;
import net.omny.utils.ConfigFile;
import net.omny.utils.Debug;
import net.omny.utils.Ex;

public abstract class WebServer {

	/**
	 * Create a webserver builder
	 * 
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
	 * 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param webServer The WebServer instance
	 * @date 08/08/2021
	 */
	public static void launch(WebServer webServer) {
		if (webServer.threadPool == null) {
			// Thread pool was not provided
			// 4 default
			// TODO replace this value with either value from configFile or value depending
			// on system capabilities
			webServer.threadPool = Executors.newScheduledThreadPool(4);
		}

		webServer.init();

		webServer.threadPool.submit(() -> {
			// Run the server
			try (ServerSocket serverSocket = new ServerSocket(webServer.port)) {
				Debug.debug("Thread pool has " + webServer.threadPoolSize + " threads.");
				Debug.debug("Listening on port " + webServer.port);
				webServer.running.set(true);
				while (webServer.running.get()) {
					Socket client = serverSocket.accept();
					webServer.threadPool.submit(
							() -> {
								Debug.debug(
										Thread.currentThread().getName() + " is handling " + client.getInetAddress());
								Ex.grab(() -> webServer.handler(client));
							});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

	// Default fields
	@Getter
	private Router router = new Router(this);
	@Getter
	protected int port = (int) ConfigFile.DEFAULT_PORT;
	@Getter
	private final AtomicBoolean running = new AtomicBoolean(false);

	@Getter
	private int threadPoolSize;
	@Getter
	private ScheduledExecutorService threadPool;
	@Getter
	private CachingRequest caching;

	public WebServer(String configFile) {
		this();
		// TODO Load config file
		Toml toml = initConstructor(configFile);

		int threadCount = toml.getLong(ConfigFile.THREAD_COUNT, -1L).intValue();
		if (threadCount == -1) {
			// Determine number of thread depending on system capabilities
			threadCount = Runtime.getRuntime().availableProcessors() / 2;
		}
		this.threadPool = Executors.newScheduledThreadPool(threadCount);
		this.threadPoolSize = threadCount;

		postInit();
	}

	public WebServer(String configFile, ScheduledExecutorService threadPool, int threadPoolSize) {

		initConstructor(configFile);

		this.threadPool = threadPool;
		this.threadPoolSize = threadPoolSize;

		postInit();
	}

	public WebServer() {
	}

	private Toml initConstructor(String configFile) {
		Toml toml = new Toml().read(new File(configFile));
		this.port = toml.getLong(ConfigFile.PORT, ConfigFile.DEFAULT_PORT).intValue();
		return toml;
	}

	private void postInit() {
		this.caching = new CachingRequest(this);
	}

	private void init() {
		// TODO init the web server
		// -> handling routes
		// -> FUTURE : handling middleware
		route(this.router);
		this.router.setRouted(true);
	}

	/**
	 * Perform background task
	 * Submit the runnable in the Thread Pool from webserver
	 * 
	 * @param runnable The function to run in background
	 */
	public void background(Runnable runnable) {
		this.threadPool
				.submit(runnable);
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
	 * 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param clientSocket
	 * @throws IOException
	 * @date 08/08/2021
	 */
	public void handler(Socket clientSocket) throws IOException {
		Debug.reset("handle_request");

		BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String headerLine = br.readLine();
		try {
			if (headerLine == null) {
				throw new MalformedRequestException("readLine() returns 'null'");
			}
			Request request = Request.lightWeight(headerLine);

			int count = this.caching.countRequest(request.getPath());
			if (count > 0) {
				byte[] rawResponse = this.caching.get(request.getPath());
				Debug.debug("Accessed cached request '" + request.getPath() + "' (access : " + count + ")");
				clientSocket.getOutputStream().write(rawResponse);
				try {
					this.caching.cacheRequest(request.getPath());
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			} else {
				StringBuilder requestBuilder = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null && !line.isBlank()) {
					// Following the RFC at this link: https://datatracker.ietf.org/doc/html/rfc2616
					// Line breaking is describe as CR LF => \r \n
					requestBuilder.append(line + "\r\n");
				}
				request.readFurther(requestBuilder.toString());
				this.router.handleRoute(this, request, clientSocket);
			}
			clientSocket.close();
			Debug.time("handle_request", request.getMethod() + " on '" + request.getPath() + "' processed in {ms} ms.");
		} catch (MalformedRequestException e) {
			if (!clientSocket.isClosed()) {
				this.router.sendMalformed(clientSocket);
				clientSocket.close();
			}
		}

	}

}
