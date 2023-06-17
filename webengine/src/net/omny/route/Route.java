package net.omny.route;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import net.omny.route.impl.FileRoute;
import net.omny.route.impl.LoadedFileRoute;
import net.omny.route.impl.TemplateRoute;
import net.omny.route.impl.TextRoute;
import net.omny.views.View;

public abstract class Route {

  /**
   * Create a route from a file
   *
   * @param path The path to the file
   * @return The route
   */
  public static Route fromFile(String path) { return fromFile(path, false); }

  /**
   * Create a route from a file
   *
   * @param path The path to the file
   * @param preloaded If the file is preloaded
   * @return The route
   */
  public static Route fromFile(String path, boolean preloaded) {
    return preloaded ? new LoadedFileRoute(path) : new FileRoute(path);
  }

  /**
   * Create a route from a file
   *
   * @param text The text to display
   * @return The route
   */
  public static Route fromText(String text) { return new TextRoute(text); }

  /**
   * Create a route from a template
   *
   * @param file The template file
   * @param variables The variables to replace
   * @return The route
   */
  public static Route fromTemplate(String file, Map<String, String> variables) {
    return new TemplateRoute(file, (r) -> variables);
  }

  @Getter @Setter private boolean allowCache = true;
  @Getter @Setter private long lastInCache = 100;
  @Getter @Setter private String path;
  @Getter @Setter private Method method;

  public abstract View handle(Request req, Response res);
}
