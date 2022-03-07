package net.omny.route.impl;

import java.util.function.BiFunction;

import net.omny.route.Request;
import net.omny.route.Response;
import net.omny.route.Route;
import net.omny.views.View;

public class AnonymousRoute extends Route{

    private BiFunction<Request, Response, View> route;

    public AnonymousRoute(BiFunction<Request,Response,View> route) {
        this.route = route;
    }
    
    @Override
    public View handle(Request req, Response res) {
        return route.apply(req, res);
    }
    
}
