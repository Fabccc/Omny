package net.omny.test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import net.omny.route.HTTP;
import net.omny.route.NamedRouter;
import net.omny.route.Request;
import net.omny.route.Response;
import net.omny.route.Route;
import net.omny.route.Router;
import net.omny.route.RouterOptions;
import net.omny.route.impl.FileRoute;
import net.omny.route.impl.TextRoute;
import net.omny.server.WebServer;
import net.omny.utils.Debug;
import net.omny.views.TextView;
import net.omny.views.View;

public class Test extends WebServer{


	/*
	Brian Goetz in his famous book "Java Concurrency in Practice" recommends the following formula:

	Number of threads = Number of Available Cores * (1 + Wait time / Service time)

	*/
	private static final int THREAD_COUNT = 12;
	private static final ScheduledExecutorService SERVICE = Executors.newScheduledThreadPool(THREAD_COUNT);
	
	public static void main(String[] args) {
		Debug.ENABLE = true;
		launch(new Test());
	}
	
	public Test() {
		super("conf.toml", SERVICE, THREAD_COUNT);
	}
	
	@Override
	public void route(Router router) {
		router.route(TestRouter.class);
		router.route(NamespaceTestRouter.class);
		router.route(new NestedRouter1());
		router.staticRoute("./webengine/static");
	}
	
	public static class TestRouter {
		
		@HTTP(url = "/")
		public Route indexRoute = new FileRoute("webengine/index.html");

		@HTTP(url = "/loulou")
		public Route fileRoute = new FileRoute("webengine/loulou.json");
		
		@HTTP(url = "/trolol")
		public View index(Request req, Response res) {
			res.setHeader("Content-Type", "text/plain");
			return new TextView("trolololllololololololololololo");
		}
		
	}

	@RouterOptions(namespace = "/test")
	public static class NamespaceTestRouter{

		@HTTP(url = "user")
		public Route userApi = new TextRoute("Oula bizarrrreee");

	}

	public static class NestedRouter1 extends NamedRouter{

		public NestedRouter1() {
			super("api");
		}

		@Override
		public Router route(Router router) {
			// TODO Auto-generated method stub
			return super.route(router);
		}

	}

	public static class NestedRouter2 extends NamedRouter{

		public NestedRouter2() {
			super("user");
		}

		@Override
		public Router route(Router router) {
			// TODO Auto-generated method stub
			return super.route(router);
		}

	}

}
