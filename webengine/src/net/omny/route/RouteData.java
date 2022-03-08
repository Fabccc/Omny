package net.omny.route;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class RouteData {

  @Getter
  private Route route;
  @Getter
  private boolean isStatic;
  private boolean param;

  @Getter
  private List<Integer> paramsIndex = new ArrayList<>();

  public RouteData(Route route, boolean isStatic, String path) {
    this.route = route;
    this.isStatic = isStatic;
    this.param = checkParam(path);
  }

  private boolean checkParam(String path) {
    String[] decomp = path.split("/");
    for (int i = 0; i < decomp.length; i++) {
      String str = decomp[i];
      if (str.startsWith(":")) {
        paramsIndex.add(i);
      }
    }
    return !paramsIndex.isEmpty();
  }

  /**
   * @return the param
   */
  public boolean hasParam() {
    return param;
  }

  @Override
  public String toString() {
    return "RouteData {route=" + route + ", isStatic=" + isStatic + "}";
  }

}
