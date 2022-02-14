package net.omny.route;

import net.omny.views.View;

public interface Route {
	
	View handle(Request req, Response res);
	
}
