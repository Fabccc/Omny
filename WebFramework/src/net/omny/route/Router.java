package net.omny.route;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import net.omny.utils.Debug;
import net.omny.views.View;

public class Router {

	private Map<String, Route> routes = new HashMap<>();

	public Router() {
		
	}
	
	public Router route(Router router) {
		this.routes.putAll(router.routes);
		return this;
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
		// TODO Find all routes, using annotations
		Class<?> clazz = object.getClass();
		// Get all the method 
		return this;
	}
	
	
	public Router route(String path, Route route) {
		this.routes.put(path, route);
		return this;
	}
	
	public Router route(String path, String filePath) {
		return route(path, new FileRoute(filePath));
	}
	
	/**
	 * 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param request The request of the client
	 * @return true if at least one route is the path, false otherwise
	 * @date 15/08/2021
	 */
	public boolean handleRoute(Request request, Socket client) throws IOException{
		//TODO static files
		
		// Dynamic routing
		RouteLoop: for(Map.Entry<String, Route> path : this.routes.entrySet()) {
			// Split the current path to the divison
			// "/foo/bar/baz" => ["foo", "bar", "baz"]
			// Used to detect the params in URL
			
			// For example if our router register a route like this "/player/:id"
			// We can detect the route "/player/54" (the value in params is treated as String"
			
			
			//TODO use Regex for better handling
			String[] division = path.getKey().split("\\\\");
			String[] currentUrlDivision = request.getPath().split("\\\\");
			
			// The number of division is different from the current request division 
			if(division.length != currentUrlDivision.length) {
				continue;
			}
			// We compare each division
			Map<String, String> params = new HashMap<>();
			for(int i = 0; i < currentUrlDivision.length; i++) {
				if(!division[i].equals(currentUrlDivision[i])) {
					// Path are different
					continue RouteLoop;
				}
				if(division[i].startsWith(":")) {
					// If it's a URL param
					String paramName = division[i].substring(1);
					String paramValue = currentUrlDivision[i];
					params.put(paramName, paramValue);
				}
			}

			Debug.debug("Found route for "+request.getPath());
			
			// current division path are the same
			// It's the same route
			request.setParams(params);
			Response response = new Response(request);
			
			View view = path.getValue().handle(request, response);
			view.write(response);
			// The content length of the response body, in bytes

			BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			// Writing header
			// Writing body content
			// End of HTTP response following the HTTP specs
			clientWriter.write(response.toString());
			// Flush the stream
			clientWriter.flush();
			
			return true;
		}
		// Returning a 404 Not Found
		Response response = new Response(request);
		response.setResponseCode(Code.E404_NOT_FOUND);
		BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		// Writing header
		clientWriter.write(response.toString());
		// End of HTTP response following the HTTP specs
		clientWriter.write("\r\n");
		// Flush the stream
		clientWriter.flush();
		return false;
	}
	
}
