package net.omny.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.omny.route.HTTP;
import net.omny.route.Request;
import net.omny.route.Response;
import net.omny.route.Route;
import net.omny.route.Router;
import net.omny.route.impl.FileRoute;
import net.omny.server.WebServer;
import net.omny.views.TextView;
import net.omny.views.View;

public class Test extends WebServer{

	private static final ExecutorService SERVICE = Executors.newScheduledThreadPool(6);
	
	public static void main(String[] args) {
		launch(new Test());
	}
	
	public Test() {
		super("./conf.toml", SERVICE);
	}
	
	@Override
	public void route(Router router) {
		router.route(TestRouter.class);
		router.staticRoute("./static");
	}
	
	public static class TestRouter {
		
		@HTTP(url = "/")
		public Route indexRoute = new FileRoute("index.html");

		@HTTP(url = "/loulou")
		public Route fileRoute = new FileRoute("loulou.json");
		
		@HTTP(url = "/trolol")
		public View index(Request req, Response res) {
			res.setHeader("Content-Type", "text/plain");
			return new TextView("trolololllololololololololololo");
		}
		
	}

}
