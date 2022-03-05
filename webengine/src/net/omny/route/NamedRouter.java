package net.omny.route;

import lombok.Getter;

public class NamedRouter extends Router {

    @Getter
    private String namespace;

    public NamedRouter(String namespace) {
        this.namespace = namespace;
    }

}
