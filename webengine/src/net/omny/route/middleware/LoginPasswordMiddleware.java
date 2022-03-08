package net.omny.route.middleware;

import java.util.function.BiFunction;

import org.codehaus.plexus.util.Base64;

import net.omny.route.Request;
import net.omny.utils.HTTPUtils.Headers;

public abstract class LoginPasswordMiddleware extends AuthentificationMiddleware {

    public static LoginPasswordMiddleware middleware(BiFunction<String, String, Boolean> auth) {
        return new LoginPasswordMiddleware() {
            @Override
            public boolean auth(String login, String password) {
                return auth(login, password);
            }
        };
    }

    public static LoginPasswordMiddleware middleware(String url, BiFunction<String, String, Boolean> auth) {
        return new LoginPasswordMiddleware(url) {
            @Override
            public boolean auth(String login, String password) {
                return auth(login, password);
            }
        };
    }

    public LoginPasswordMiddleware(String url) {
        super(url);
    }

    public LoginPasswordMiddleware() {
        super("");
    }

    @Override
    public boolean auth(Request request) {
        if (request.containsHeader(Headers.AUTHORIZATION))
            return false;
        String auth = request.getHeader(Headers.AUTHORIZATION);
        if (!auth.startsWith("Basic ")) {
            return false;
        }
        String base64 = auth.substring("Basic ".length());
        String[] credentials = new String(Base64.decodeBase64(base64.getBytes())).split("\\:");
        String login = credentials[0];
        String password = credentials[1];
        return auth(login, password);
    }

    public abstract boolean auth(String login, String password);

    @Override
    public String toString() {
        return "LoginPasswordMiddleware { url=" + getUrl() + "}";
    }

}
