package net.omny;

import net.omny.route.Router;

public abstract class WebServer {

	public static void launch(WebServer webServer) {
		
	}
	
	private Router router;
	private WebServerConfig config;
	
	public WebServer(String configFile) {
		
	}
	
	public abstract void route(Router router);
	
}
