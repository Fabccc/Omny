package net.omny.route.middleware;

import net.omny.route.Request;
import net.omny.utils.HTTPUtils.Headers;

public abstract class LoginPasswordMiddleware extends AuthentificationMiddleware{

    public LoginPasswordMiddleware(String url) {
        super(url);
    }

    public LoginPasswordMiddleware() {
        super("");
    }

    @Override
    public boolean auth(Request request) {
        String auth = request.getHeader(Headers.AUTHORIZATION);
        if(!auth.startsWith("Basic ")){
            return false;
        }
        // TODO
        return false;
    }

    public abstract boolean auth(String login, String password);
    
}
