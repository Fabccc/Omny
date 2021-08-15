package net.omny.test;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.omny.route.Method;
import net.omny.route.Request;
public class RequestTest{

  public static final String REQUEST = 
    "GET / HTTP/1.1\r\nHost: localhost:8080\r\nConnection: keep-alive";


  @Test
  public void testRequestParsing(){
    Request request = Request.parse(REQUEST);

    assertEquals(Method.GET, request.getMethod());
    assertEquals("/", request.getPath());
    assertEquals("localhost:8080", request.getHeader("Host"));
    assertEquals("keep-alive", request.getHeader("connection"));
  }

}