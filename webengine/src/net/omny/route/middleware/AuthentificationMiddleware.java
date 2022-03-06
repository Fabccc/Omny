package net.omny.route.middleware;

import java.io.IOException;
import java.net.Socket;

import net.omny.route.Request;
import net.omny.route.Router;
import net.omny.server.WebServer;
import net.omny.utils.HTTPUtils.Headers;

public abstract class AuthentificationMiddleware implements Middleware, UrlMiddleware {

  private String url;

  public AuthentificationMiddleware(String url) {
    this.url = url;
  }

  @Override
  public boolean handle(WebServer webServer, Router router, Request request, Socket client) throws IOException {
    if (request.equalsPath(url, true))
      return false;
    if (!request.containsHeader(Headers.AUTHORIZATION))
      return false;
    return auth(request);
  }

  public abstract boolean auth(Request request);

  @Override
  public String getUrl() {
      return this.url;
  }

  @Override
  public void setUrl(String url) {
      this.url = url;
  }

}
