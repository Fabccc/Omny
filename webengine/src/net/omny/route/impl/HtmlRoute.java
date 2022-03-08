package net.omny.route.impl;

import net.omny.route.Request;
import net.omny.route.Response;
import net.omny.route.Route;
import net.omny.utils.HTTPUtils.Headers;
import net.omny.utils.HTTPUtils.MimeType;
import net.omny.views.TextView;
import net.omny.views.View;

public class HtmlRoute extends Route {

    private TextView view;

    public HtmlRoute(String text) {
        this.view = new TextView(text);
    }

    @Override
    public View handle(Request req, Response res) {
        res.setHeader(Headers.CONTENT_TYPE, MimeType.HTML);
        return this.view;
    }

}
