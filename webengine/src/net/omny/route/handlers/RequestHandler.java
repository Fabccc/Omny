package net.omny.route.handlers;

import java.io.IOException;
import java.net.Socket;

import net.omny.route.Request;
import net.omny.route.Router;
import net.omny.server.WebServer;

/**
 * Interface 
 */
public interface RequestHandler {
  
  /**
   * Handling request from a client
   * 
   * @param router The main router to get routes from
   * @param request The request
   * @param client The client socket
   * @return TRUE if request processing should stop, FALSE otherwise (Middleware)
   * @throws IOException
   */
  boolean handle(WebServer webServer, Router router, Request request, Socket client)
    throws IOException;

}
