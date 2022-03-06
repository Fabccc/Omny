package net.omny.test;

import java.util.function.Consumer;

import net.omny.route.Router;
import net.omny.server.WebServer;

public class DummyWebServer extends WebServer {

    private Consumer<Router> routes;

    public DummyWebServer(Consumer<Router> routes) {
        super();
        this.routes = routes;
        postInit();
    }

    @Override
    protected void init() {
		route(this.router);
		this.router.setRouted(true);
    }
    

    @Override
    public void route(Router router) {
        routes.accept(router);
    }

}
