package net.omny.test;

import net.omny.route.Router;
import net.omny.server.WebServer;

public class Test extends WebServer{

	public static void main(String[] args) {
		launch(new Test());
	}
	
	@Override
	public void route(Router router) {
		// TODO test if we can route some index.html file or even test.html
		
	}

}
