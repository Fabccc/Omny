package net.omny.views;

import com.google.gson.Gson;

import lombok.Getter;
import net.omny.route.Response;


@Getter
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


  @Override
  public String toString() {
    return "{" +
      " gson='" + getGson() + "'" +
      "}";
  }

  
}
