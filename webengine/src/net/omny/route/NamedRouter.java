package net.omny.route;

import lombok.Getter;

public class NamedRouter extends Router {

    @Getter
    private String namespace;

    public NamedRouter(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public Router route(String path, String filePath) {
        return super.route("/" + namespace + path, filePath);
    }

    @Override
    public Router route(String path, Route route, Method method) {
        return super.route("/" + namespace + path, route, method);
    }

}
