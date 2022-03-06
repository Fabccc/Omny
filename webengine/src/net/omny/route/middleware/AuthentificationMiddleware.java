package net.omny.route.middleware;

import java.io.IOException;
import java.net.Socket;

import net.omny.route.Request;
import net.omny.route.Router;
import net.omny.server.WebServer;

public abstract class AuthentificationMiddleware implements Middleware{

  private String url;

  public AuthentificationMiddleware(String url){
    this.url = url;
  }

  @Override
  public boolean handle(WebServer webServer, Router router, Request request, Socket client) throws IOException {
    // TODO Auto-generated method stub
    return false;
  }
  


}
