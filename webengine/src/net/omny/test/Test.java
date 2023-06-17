package net.omny.test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.omny.route.Code;
import net.omny.route.HTTP;
import net.omny.route.Method;
import net.omny.route.NamedRouter;
import net.omny.route.Request;
import net.omny.route.Response;
import net.omny.route.Route;
import net.omny.route.Router;
import net.omny.route.RouterOptions;
import net.omny.route.impl.JsonRoute;
import net.omny.route.impl.TextRoute;
import net.omny.route.middleware.LoginPasswordMiddleware;
import net.omny.server.WebServer;
import net.omny.utils.Debug;
import net.omny.utils.HTTPUtils;
import net.omny.utils.HTTPUtils.MimeType;
import net.omny.views.JsonView;
import net.omny.views.TextView;
import net.omny.views.View;

public class Test extends WebServer {

  public static void main(String[] args) {
    Debug.ENABLE = true;
    launch(new Test());
  }

  public Test() { super("conf.toml"); }

  @Override
  public void route(Router router) {
    router.route(TestRouter.class);
    router.route(NamespaceTestRouter.class);
    router.route(new DashboardRouter());
    router.route(new NestedRouter1());
    router.staticRoute("./webengine/static");
  }

  public static class TestRouter {

    @HTTP(url = "/")
    public Route indexRoute = Route.fromFile("webengine/index.html", true);

    @HTTP(url = "/loulou")
    public Route fileRoute = Route.fromFile("webengine/loulou.json");

    @HTTP(url = "/test")
    public View index(Request req, Response res) {
      res.setHeader("Content-Type", "text/plain");
      return new TextView("trolololllololololololololololo");
    }

    @HTTP(url = "/test2")
    public Route templateRoute = Route.fromTemplate(
        "template_test.html", Map.of("name", "Hello world !"));
  }

  @RouterOptions(namespace = "/test")
  public static class NamespaceTestRouter {

    @HTTP(url = "user")
    public Route userApi = new TextRoute("Oula bizarrrreee");
  }

  public static class DashboardRouter extends NamedRouter {

    public DashboardRouter() { super("dashboard"); }

    @Override
    public void route() {
      middleware(LoginPasswordMiddleware.middleware((login, password) -> true));
                        routeHtml("/admin", """
						<html>
							<body>
								<h1>Bienvenu, Admin ! </h1>
							</body>
						</html>
					""");
    }
  }

  public static class NestedRouter1 extends NamedRouter {

    public NestedRouter1() { super("api"); }

    @Override
    public void route() {
      route(new NestedRouter2());
    }
  }

  public static class NestedRouter2 extends NamedRouter {

    public NestedRouter2() { super("user"); }

    @Override
    public void route() {
      route("/name", new ApiUserNameRoute(), Method.GET);
      route(new ApiUserProfileRoute());
    }
  }

  public static class ApiUserProfileRoute extends JsonRoute {

    public ApiUserProfileRoute() {
      setAllowCache(false);
      setPath("/name/:uuid");
      setMethod(Method.GET);
    }

    @Override
    public View handle(Request req, Response res) {
      super.handle(req, res);
      String s = req.getParams("uuid");
      if (s == null) {
                                return new HTTPUtils.ErrorView(Code.E400_BAD_REQUEST,
						"""
									{
          "error" : "Missing parameter"
									}
								""", MimeType.JSON);
      }

                        return new JsonView("""
					{
        "uuid" : "%s", "name" : "%s"
					}
					""".formatted(s, "LeTroll :pingching_hand:"));
    }
  }

  public static class ApiUserNameRoute extends JsonRoute {

    private AtomicInteger test;

    public ApiUserNameRoute() {
      this.test = new AtomicInteger();
      this.setAllowCache(true);
      this.setLastInCache(100);
    }

    @Override
    public View handle(Request req, Response res) {
      super.handle(req, res);
                        return new JsonView("""
					{
        "value" : % d
					}
					""".formatted(test.incrementAndGet()));
    }
  }
}
