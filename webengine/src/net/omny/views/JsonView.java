package net.omny.views;

import com.google.gson.Gson;

import net.omny.route.Response;

public class JsonView implements View{

  private String gson;

  public JsonView(Gson gson){
    this.gson = gson.toString();
  }

  public JsonView(String string) {
    this.gson = string;
  }

  @Override
  public void write(Response res) {
    res.addBody(gson);
  }
  
}
