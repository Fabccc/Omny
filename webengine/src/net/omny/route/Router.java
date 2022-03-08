package net.omny.route;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import joptsimple.internal.Objects;
import lombok.Getter;
import lombok.Setter;
import net.omny.route.impl.AnonymousRoute;
import net.omny.route.impl.FileRoute;
import net.omny.route.impl.HtmlRoute;
import net.omny.route.impl.LoadedFileRoute;
import net.omny.route.middleware.Middleware;
import net.omny.route.middleware.MiddlewarePriority;
import net.omny.route.middleware.UrlMiddleware;
import net.omny.server.WebServer;
import net.omny.utils.ByteStack;
import net.omny.utils.Debug;
import net.omny.utils.Ex;
import net.omny.utils.HTTPUtils;
import net.omny.utils.HTTPUtils.Headers;
import net.omny.utils.HTTPUtils.MimeType;
import net.omny.utils.HTTPUtils.Version;
import net.omny.utils.MapUtils;
import net.omny.utils.Primitive;
import net.omny.views.View;

/**
 * 
 */
public class Router {

	public enum StaticPolicy {

		FOR_EACH_REQUEST, ON_STARTUP_LOAD, REQUEST_AND_LOAD;

		private StaticPolicy() {
		}
	}

	@Getter
	protected Map<String, Map<Method, RouteData>> routes = new HashMap<>();
	@Getter
	protected EnumMap<MiddlewarePriority, List<Middleware>> middlewares = new EnumMap<>(MiddlewarePriority.class);
	@Getter
	@Setter
	private boolean routed;
	private boolean main;

	public Router(WebServer webServer) {
		// By default
		// This default handler handle static routing
		// And non-params URL dependent
		this.main = true;
	}

	public Router() {
		this.routed = false;
		this.main = false;
	}

	public void route() {
	}

	// =========================================
	// Routing functions

	public Router route(Router router) {
		if (router.isRouted())
			return this;
		router.route();
		if (router instanceof NamedRouter namedRouter) {
			var routes = router.routes.entrySet().stream()
					.map(e -> MapUtils.changeEntry(e, path -> "/" + namedRouter.getNamespace() + path))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			if (main) {
				for (var entry : routes.entrySet()) {
					for (var methods : entry.getValue().entrySet()) {

						Debug.debug("Routing {" + entry.getKey() + "} [dynamic " + methods.getKey().toString() + "]");
					}
				}
			}

			var middlewares = router.middlewares
					.entrySet()
					.stream()
					.map(entry -> MapUtils.changeValue(entry, list -> list.stream().peek(middleware -> {
						if (middleware instanceof UrlMiddleware urlMiddleware) {
							urlMiddleware.setUrl("/" + namedRouter.getNamespace() + urlMiddleware.getUrl());
						}
					}).toList()))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			addMiddlewaresAndRoutes(routes, middlewares);
		} else {
			addMiddlewaresAndRoutes(router.routes, router.middlewares);
		}

		router.setRouted(true);
		return this;
	}

	protected void addMiddlewaresAndRoutes(Map<String, Map<Method, RouteData>> routes,
			Map<MiddlewarePriority, List<Middleware>> middlewares) {
		// Merge routes
		for (String path : routes.keySet()) {
			// Path is already contained
			if (this.routes.containsKey(path)) {
				for (Method method : routes.get(path).keySet()) {
					// We don't override
					if (!this.routes.get(path).containsKey(method)) {
						this.routes.get(path).put(method, routes.get(path).get(method));
					}
				}
			} else {
				// Path is not contained
				this.routes.putAll(new HashMap<>() {
					{
						put(path, routes.get(path));
					}
				});
			}
		}

		// Merge middlewares
		for (MiddlewarePriority priority : middlewares.keySet()) {
			if (this.middlewares.containsKey(priority)) {
				this.middlewares.get(priority).addAll(middlewares.get(priority));
			} else {
				this.middlewares.putAll(new HashMap<>() {
					{
						put(priority, middlewares.get(priority));
					}
				});
			}
		}
	}

	protected void appendRoutes(Router source, Router destination) {
		destination.middlewares.putAll(source.middlewares);
	}

	/**
	 * 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param object
	 * @return
	 * @date 17/08/2021
	 */
	public Router route(Class<?> clazz) {
		// Get all the method
		return Ex.grab(() -> {
			Object routesObj = clazz.getConstructor().newInstance();
			return route(routesObj);
		});
	}

	/**
	 * 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param object
	 * @return
	 * @date 17/08/2021
	 */
	public Router route(Object object) {
		Class<?> clazz = object.getClass();
		String nameSpace = HTTPUtils.DEFAULT_NAMESPACE;

		if (clazz.isAnnotationPresent(RouterOptions.class)) {
			RouterOptions options = clazz.getAnnotation(RouterOptions.class);
			nameSpace = options.namespace();
		}

		for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
			if (method.canAccess(object)) {
				if (method.isAnnotationPresent(HTTP.class)) {
					// This is a routable function
					Class<?> returnType = method.getReturnType();
					if (View.class.isAssignableFrom(returnType)) {
						// The return type is implementing View interface
						if (method.getParameterCount() == 2) {
							if (method.getParameterTypes()[0] == Request.class
									&& method.getParameterTypes()[1] == Response.class) {
								// Both res and req are present as parameter
								HTTP annotation = method.getAnnotation(HTTP.class);
								// We get annotation content
								String url = annotation.url();
								if (!nameSpace.equals(HTTPUtils.DEFAULT_NAMESPACE)) {
									url = nameSpace + "/" + url;
								}
								if (main)
									Debug.debug(
											"Routing {" + url + "} [dynamic " + annotation.method().toString() + "]");
								route(url, (req, res) -> {
									return Ex.grab(() -> (View) method.invoke(object, req, res));
								}, annotation.method());
							}
						}
					}
				}
			}
		}

		for (Field field : clazz.getDeclaredFields()) {
			if (field.canAccess(object)) {
				if (field.isAnnotationPresent(HTTP.class)) {
					if (Route.class.isAssignableFrom(field.getType())) {
						HTTP annotation = field.getAnnotation(HTTP.class);
						String url = annotation.url();
						if (!nameSpace.equals(HTTPUtils.DEFAULT_NAMESPACE)) {
							url = nameSpace + "/" + url;
						}
						if (main)
							Debug.debug("Routing {" + url + "} [dynamic " + annotation.method().toString() + "]");
						route(url, Ex.grab(() -> (Route) field.get(object)), annotation.method());
					}
				}
			}
		}

		// Get all the method
		return this;
	}

	/**
	 * Static routing for files like CSS, JS etc...
	 * 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param staticFolder path to folder (relative)
	 * @return the router
	 * @date 22/08/2021
	 */
	public Router staticRoute(String staticFolder) {
		return staticRoute(staticFolder, StaticPolicy.ON_STARTUP_LOAD);
	}

	/**
	 * Static routing for files like CSS, JS etc...
	 * 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param staticFolder staticFolder path to folder (relative)
	 * @param policy       Policy of static files routing
	 * @return
	 * @date 22/08/2021
	 */
	public Router staticRoute(String staticFolder, StaticPolicy policy) {
		if (policy == null)
			return this;
		if (policy == StaticPolicy.ON_STARTUP_LOAD) {
			File rootFolder = new File(staticFolder);
			if (rootFolder.isFile()) {
				throw new IllegalArgumentException("Require a folder, file was provide");
			}
			try {
				for (File subFile : rootFolder.listFiles())
					routeFile("", subFile);
			} catch (Exception e) {
				if (Debug.ENABLE) {
					e.printStackTrace();
				}
			}
		}
		return this;
	}

	/**
	 * Return a list of middleware that is instanceof the class
	 * And has a specified priority
	 * 
	 * @param <T>      the type of Middleware
	 * @param priority the priority
	 * @param clazz
	 * @return The list of all the middleware corresponding to this parameters
	 */
	@SuppressWarnings("unchecked")
	public <T extends Middleware> List<T> getMiddlewares(MiddlewarePriority priority, Class<? extends T> clazz) {
		return this.middlewares.get(priority)
				.stream()
				.filter(s -> clazz.isAssignableFrom(s.getClass()))
				.map(s -> (T) s)
				.toList();
	}

	private void routeFile(String path, File file) {
		if (file.isDirectory()) {
			for (File subFile : file.listFiles())
				routeFile(path + "/" + file.getName(), subFile);
		}
		if (main)
			Debug.debug("Routing {" + path + "/" + file.getName() + "} [static]");
		route(path + "/" + file.getName(), new LoadedFileRoute(file), Method.GET, true);
	}

	public Router routeHtml(String path, String htmlContent) {
		Objects.ensureNotNull(path);
		Objects.ensureNotNull(htmlContent);
		return route(path, new HtmlRoute(htmlContent), Method.GET);
	}

	public Router route(Route route) {
		Objects.ensureNotNull(route.getMethod());
		return route(route, route.getMethod());
	}

	public Router route(Route route, Method method) {
		Objects.ensureNotNull(route.getPath());
		return route(route.getPath(), route, method, false);
	}

	public Router route(String path, BiFunction<Request, Response, View> route, Method method) {
		return route(path, new AnonymousRoute(route), method);
	}

	public Router route(String path, Route route, Method method) {
		return route(path, route, method, false);
	}

	public Router route(String path, Route route, Method method, boolean isStatic) {
		if (this.routes.containsKey(path)) {
			// It already contains path with a map
			if (this.routes.get(path).containsKey(method)) {
				// It already have a map with same path and same method
				// we throw exception
				throw new IllegalStateException("There is already a route with this path AND method");
			} else
				// We add this routes
				this.routes.get(path).put(method, new RouteData(route, isStatic, path));
		} else {
			// there is no path with this name
			Map<Method, RouteData> map = new HashMap<>();
			map.put(method, new RouteData(route, isStatic, path));
			this.routes.put(path, map);
		}
		return this;
	}

	/**
	 * Add a route to the router with the specified path, that returns the content
	 * of the file
	 * 
	 * @param path
	 * @param filePath The path to the file
	 * @return this
	 */
	public Router route(String path, String filePath) {
		return route(path, new FileRoute(filePath), Method.GET);
	}

	// =========================================
	// Handlers functions

	/**
	 * Add an handler to the current list of request handlers
	 * 
	 * @param handler The request handler
	 * @return The router object
	 */
	public Router middleware(Middleware handler) {
		return middleware(handler, MiddlewarePriority.BEFORE);
	}

	/**
	 * Add an handler to the current list of request handlers with priority defined
	 * 
	 * @param handler  The request handler
	 * @param priority The priority of this handler
	 * @return The router object
	 */
	public Router middleware(Middleware handler, MiddlewarePriority priority) {
		if (this.middlewares.containsKey(priority)) {
			// This priority already exists and there is a list linked to
			this.middlewares.get(priority).add(handler);
		} else {
			// This priority doesn't exists
			// We must create a new List containing the handler
			// Then put it to this priority
			List<Middleware> list = new ArrayList<>();
			list.add(handler);
			this.middlewares.put(priority, list);
		}
		Debug.debug("Middleware {" + handler + " }");
		return this;
	}

	/**
	 * Handle routing and finding response
	 * 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param request The request of the client
	 * @param client  The socket to write to
	 * @return true if at least one route is the path, false otherwise
	 * @date 15/08/2021
	 */
	public boolean handleRoute(WebServer webServer, Request request, Socket client) throws IOException {

		// Processing request middlewares...
		Debug.debug("Middlewares : " + this.middlewares.getOrDefault(MiddlewarePriority.BEFORE, List.of()));
		for (Middleware middleware : this.middlewares.getOrDefault(MiddlewarePriority.BEFORE, List.of())) {
			Debug.debug("Checking " + middleware);
			Debug.debug("Checking " + middleware.getClass().getCanonicalName());
			if (middleware instanceof UrlMiddleware urlMiddleware) {
				if (!request.getPath().startsWith(urlMiddleware.getUrl())) {
					Debug.debug("Skip middleWare " + middleware.getClass().getSimpleName() + " url "
							+ urlMiddleware.getUrl());
					continue;
				}
			}
			if (middleware.handle(webServer, this, request, client)) {
				// If handler returns true
				// Then we must stop processing more
				return true;
			}
		}

		// Dynamic routing
		RouteLoop: for (String path : this.routes.keySet()) {
			// Split the current path to the divison
			// "/foo/bar/baz" => ["foo", "bar", "baz"]
			// Used to detect the params in URL

			// For example if our router register a route like this "/player/:id"
			// We can detect the route "/player/54" (the value in params is treated as
			// String"

			// TODO use Regex for better handling
			String[] division = path.split("\\/");
			String[] currentUrlDivision = request.getPath().split("\\/");

			// The number of division is different from the current request division
			if (division.length != currentUrlDivision.length) {
				continue;
			}

			RouteData routeData = null;
			if ((routeData = this.routes.get(path).get(request.getMethod())) == null) {
				// Path exist but not with this method
				continue RouteLoop;
			}

			// current division path are the same
			// The path in the route has params, but not the request
			request.setParams(request.extractParams(path));
			if (!request.equalsPath(path, true)) {
				continue RouteLoop;
			}
			// We compare each division that are not params

			// It's the same route
			Debug.debug("Requests param " + request.getParams() + " / " + routeData.hasParam());
			Debug.debug("Found dynamic route for " + request.getPath());
			Debug.debug("RouteData " + routeData + " / " + path);

			sendCorrect(webServer, client, routeData, request);
			return true;
		}
		// Returning a 404 Not Found
		// IT'S VERY IMPORTANT, IT MUST STAY AT THE END OF EVERY ROUTES
		Response response = new Response(request);
		response.setResponseCode(Code.E404_NOT_FOUND);
		response.setBinary(false);
		response.setCharset("UTF-8");
		response.setHeader(Headers.CONTENT_TYPE, MimeType.HTML);

		var clientStream = client.getOutputStream();
		var clientStreamWriter = new OutputStreamWriter(clientStream);

		BufferedWriter clientWriter = new BufferedWriter(clientStreamWriter);
		// Writing header
		clientWriter.write(response.toString());
		// Flush the stream
		clientWriter.flush();
		Debug.debug("404 error for '" + request.getPath() + "'");
		return false;
	}

	public void sendCorrect(WebServer webServer, Socket client, RouteData routeData, Request request)
			throws IOException {
		Response response = new Response(request);

		View view = routeData.getRoute().handle(request, response);
		view.write(response);
		// The content length of the response body, in bytes

		if (response.isBinary()) {
			Debug.debug("File is binary");
			client.getOutputStream().write(response.toString().getBytes(StandardCharsets.UTF_8));

			client.getOutputStream().write(Primitive.toArray(response.getBody()));

			client.getOutputStream().write(HTTPUtils.DOUBLE_CRLF_AS_BYTES);

			client.getOutputStream().flush();
		} else {
			BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			// Writing header
			// Writing body content
			// End of HTTP response following the HTTP specs
			clientWriter.write(response.toString());
			// Flush the stream
			clientWriter.flush();
		}

		if (routeData.getRoute().isAllowCache()) {
			if (webServer.getCaching().countRequest(request.getPath()) == 0) {
				// we must cache it
				ByteStack byteContent = new ByteStack();
				if (response.isBinary()) {
					byteContent.push(response.toString().getBytes(StandardCharsets.UTF_8));
					byteContent.push(response.getBody().getBackedArray());
					byteContent.push(HTTPUtils.DOUBLE_CRLF_AS_BYTES);
				} else {
					byteContent.push(response.toStringAsByte());
				}
				webServer.getCaching().cacheRequest(request.getPath(), byteContent.getBackedArray(),
						routeData.getRoute().getLastInCache());
				Debug.debug("caching request ");

			} else {
				webServer.getCaching().updateCache();
			}
		}
	}

	public void sendMalformed(Socket client) {
		try {
			Debug.debug("Handling malformed request");
			var clientStream = client.getOutputStream();

			Response response = new Response(Code.E400_BAD_REQUEST, Version.V1_1);
			response.setResponseCode(Code.E404_NOT_FOUND);

			if (client.isClosed()) {
				return;
			}
			clientStream.write(response.toStringAsByte());
			clientStream.write(HTTPUtils.DOUBLE_CRLF_AS_BYTES);
			clientStream.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
