package net.omny.route.impl;

import com.google.gson.Gson;

import lombok.Getter;
import net.omny.route.Request;
import net.omny.route.Response;
import net.omny.route.Route;
import net.omny.utils.HTTPUtils.Headers;
import net.omny.views.JsonView;
import net.omny.views.View;

public class JsonRoute implements Route{

  @Getter
  private JsonView view;

  public JsonRoute(Gson gson){
    this.view = new JsonView(gson);
  }

  public JsonRoute(String string){
    this.view = new JsonView(string);
  }

  @Override
  public View handle(Request req, Response res) {
    res.setHeader(Headers.CONTENT_TYPE, "application/json");
    return this.view;
  }
}
