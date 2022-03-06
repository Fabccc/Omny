package net.omny.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.omny.route.NamedRouter;
import net.omny.route.middleware.LoginPasswordMiddleware;
import net.omny.route.middleware.MiddlewarePriority;

public class RouterTest {

    @Test
    public void testRouterMiddleware() {
        DummyWebServer dummyWebServer = new DummyWebServer(router -> {
            router.route(new APIRouter());
        });

        dummyWebServer.init();

        assertEquals("/api",
                dummyWebServer.getRouter().getMiddlewares(MiddlewarePriority.BEFORE, LoginPasswordMiddleware.class)
                .get(0)
                .getUrl());
    }

    public static class APIRouter extends NamedRouter {

        public APIRouter() {
            super("api");
        }

        @Override
        public void route() {
            middleware(new LoginPasswordMiddleware() {
                @Override
                public boolean auth(String login, String password) {
                    return true;
                }
            });
        }

    }
}
