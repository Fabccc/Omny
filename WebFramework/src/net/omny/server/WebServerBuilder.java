package net.omny.server;

import java.util.HashMap;
import java.util.Map;

import net.omny.route.Route;
import net.omny.route.impl.FileRoute;

public class WebServerBuilder {

	private Map<String, Route> routes;
	
	protected WebServerBuilder() {
		this.routes = new HashMap<>();
	}
	
	public WebServerBuilder route(String path, String file) {
		return route(path, new FileRoute(file));
	}
	
	public WebServerBuilder route(String path, Route route) {
		this.routes.put(path, route);
		return this;
	}
	
	public WebServer listen(int port) {
		WebServer webServer = new DefaultWebServer(this);
		WebServer.launch(webServer);
		return webServer;
	}
	
	// TODO handle the callback func with err supply
//	public WebServer listen(int port, Consumer<Exception> err) {
//		WebServer webServer = new DefaultWebServer(this);
//		WebServer.launch(webServer);
//		return webServer;
//	}
}
