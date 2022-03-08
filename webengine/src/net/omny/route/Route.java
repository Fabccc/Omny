package net.omny.route;

import lombok.Getter;
import lombok.Setter;
import net.omny.views.View;

public abstract class Route {
	
	@Getter @Setter
	private boolean allowCache = true;
	@Getter @Setter
	private long lastInCache = 100;
	@Getter @Setter
	private String path;
	@Getter @Setter
	private Method method;

	public abstract View handle(Request req, Response res);
	
}
