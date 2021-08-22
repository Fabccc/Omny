package net.omny.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.omny.route.Router;
import net.omny.server.WebServer;

public class Test extends WebServer{

	private static final ExecutorService SERVICE = Executors.newScheduledThreadPool(4);
	
	public static void main(String[] args) {
		launch(new Test());
	}
	
	public Test() {
		super("conf.toml", SERVICE);
	}
	
	@Override
	public void route(Router router) {
		router.route("/", "index.html");
		router.route("/pdf", "admission.pdf");
		router.route("/css", "main.css");
		router.route("/js", "main.js");
		router.route("/json", "test.json");
		router.route("/csv", "test.csv");
		router.route("/test.exe", "test.exe");
	}

}
