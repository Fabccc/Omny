package net.omny.views;

import com.google.gson.Gson;

import net.omny.route.Response;

public class JsonView implements View{

  private Gson gson;

  public JsonView(Gson gson){
    this.gson = gson;
  }

  @Override
  public void write(Response res) {
    res.addBody(gson.toString());
  }
  
}
