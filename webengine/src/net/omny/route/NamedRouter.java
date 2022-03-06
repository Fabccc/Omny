package net.omny.route;

import lombok.Getter;
import net.omny.route.middleware.Middleware;
import net.omny.route.middleware.MiddlewarePriority;
import net.omny.route.middleware.UrlMiddleware;

public class NamedRouter extends Router {

    @Getter
    private String namespace;

    public NamedRouter(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public Router middleware(Middleware handler, MiddlewarePriority priority) {
        if(handler instanceof UrlMiddleware url){
            
        }
        return super.middleware(handler, priority);
    }

}
