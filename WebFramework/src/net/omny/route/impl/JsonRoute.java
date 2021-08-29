package net.omny.route.impl;

import com.google.gson.Gson;

import lombok.Getter;
import net.omny.route.Request;
import net.omny.route.Response;
import net.omny.route.Route;
import net.omny.views.JsonView;
import net.omny.views.View;

public class JsonRoute implements Route{

  @Getter
  private JsonView view;

  private JsonRoute(){
    this.view = new JsonView(new Gson());
  }

  @Override
  public View handle(Request req, Response res) {
    res.setHeader("Content-Header", "application/json");
    return this.view;
  }
}
