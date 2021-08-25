package net.omny.route;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class RouteData {
  
  @Getter
  private String url;
  @Getter
  private Route route;
  @Getter
  private Method method;

}
