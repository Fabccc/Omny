package net.omny.test;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.Test;

import net.omny.route.Request;

import com.moandjiezana.toml.Toml;
public class RequestTest{

  public static final String request = 
    "GET / HTTP/1.1\r\nHost: localhost:8080";


  @Test
  public void testRequestParsing(){
    Request request = Request.parse(request);

    assertEquals(true, true);

  }

}