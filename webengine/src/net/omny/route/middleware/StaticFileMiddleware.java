package net.omny.route.middleware;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import net.omny.route.Method;
import net.omny.route.Request;
import net.omny.route.Response;
import net.omny.route.Route;
import net.omny.route.RouteData;
import net.omny.route.Router;
import net.omny.server.WebServer;
import net.omny.utils.ByteStack;
import net.omny.utils.Debug;
import net.omny.utils.HTTPUtils;
import net.omny.views.View;

public class StaticFileMiddleware implements Middleware {

	@Override
	public boolean handle(WebServer webServer, Router router, Request request, Socket client)
			throws IOException {
		// Static routing
		Map<Method, RouteData> findRoute = null;
		if ((findRoute = router.getRoutes().get(request.getPath())) != null) {
			// Here findRoute is not null
			RouteData routeData = null;
			if ((routeData = findRoute.get(request.getMethod())) != null) {
				if (!routeData.isStatic()) {
					return false;// do not process this
				}
				// Here routeData is not null
				Route route = routeData.getRoute();

				Response response = new Response(request);

				View view = route.handle(request, response);
				view.write(response);

				if (response.isBinary()) {
					Debug.debug("File is binary");
					client.getOutputStream().write(response.toString().getBytes(StandardCharsets.UTF_8));
					client.getOutputStream().write(response.getBody().getBackedArray());
					client.getOutputStream().write(HTTPUtils.DOUBLE_CRLF_AS_BYTES);
					client.getOutputStream().flush();
				} else {
					BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
					// Writing header
					// Writing body content
					// End of HTTP response following the HTTP specs
					client.getOutputStream().write(response.toStringAsByte());
					// Flush the stream
					clientWriter.flush();
				}
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
					webServer.getCaching().cacheRequest(request.getPath(), byteContent.getBackedArray(), 10000);
					Debug.debug("caching request ");
				} else {
					webServer.getCaching().updateCache();
				}

				// We leave here, we found a route
				// We stop routing process
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "StaticFileMiddleware {}";
	}

}
