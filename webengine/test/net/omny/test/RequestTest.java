package net.omny.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.omny.exceptions.MalformedRequestException;
import net.omny.route.Method;
import net.omny.route.Request;
public class RequestTest{

  public static final String REQUEST = 
    "GET / HTTP/1.1\r\nHost: localhost:8080\r\nConnection: keep-alive";

  public static final String REQUEST_PARAMS = 
  "GET /api/user/Fabcc_c HTTP/1.1\r\nHost: localhost:8080\r\nConnection: keep-alive";

  
  @Test
  public void testRequestParsing() throws MalformedRequestException{
    Request request = Request.parse(REQUEST);
    
    assertEquals(Method.GET, request.getMethod());
    assertEquals("/", request.getPath());
    assertEquals("localhost:8080", request.getHeader("Host"));
  }
  
  @Test
  public void testRequestIgnoreCase() throws MalformedRequestException {
  	Request request = Request.parse(REQUEST);

    assertEquals("localhost:8080", request.getHeader("Host"));
    assertEquals("localhost:8080", request.getHeader("host"));
    assertEquals("localhost:8080", request.getHeader("hOSt"));
    assertEquals("localhost:8080", request.getHeader("hosT"));
  }

  @Test
  public void testRequestParams() throws MalformedRequestException{
    String paramPaths = "/api/user/:playername";
  	Request request = Request.parse(REQUEST_PARAMS);

    assertTrue(request.equalsPath(paramPaths, true));
    var params = request.extractParams(paramPaths);
    
    assertTrue(params.containsKey("playername"));
    assertEquals("Fabcc_c", params.get("playername"));
  }

}