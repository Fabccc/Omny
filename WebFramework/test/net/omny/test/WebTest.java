package net.omny.test;

import net.omny.WebServer;
import net.omny.route.Router;

public class WebTest extends WebServer{

	public static void main(String[] args) {
		
	}
	
	public WebTest(String configFile) {
		super(configFile);
	}

	@Override
	public void route(Router router) {
		//router.route("/", FileRoute.);
	}
	
	
}
