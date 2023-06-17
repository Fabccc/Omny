package net.omny.route.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import net.omny.route.Code;
import net.omny.route.Request;
import net.omny.route.Response;
import net.omny.route.Route;
import net.omny.utils.HTTPUtils.Headers;
import net.omny.utils.HTTPUtils.MimeType;
import net.omny.views.View;

public class TemplateRoute extends Route {

  // Pattern is "#{}" and inside {} is the variable name
  public static final Pattern VARIABLE_PATTERN =
      Pattern.compile("#\\{([a-zA-Z0-9]+)\\}");

  private String templateFile;

  private Function<Request, Map<String, String>> variables;

  public TemplateRoute(String templateFile,
                       Function<Request, Map<String, String>> variables) {
    this.templateFile = templateFile;
    this.variables = variables;
  }

  @Override
  public View handle(Request req, Response res) {
    return (rawResponse) -> {
      // write headers to response
      res.setBinary(false);
      res.setHeader(Headers.CONTENT_TYPE, MimeType.HTML);
      // Open the template file
      try {
        String templates =
            new String(Files.readAllBytes(Paths.get(templateFile)));
        Map<String, String> vars = variables.apply(req);
        // Replace all variables in the template using VARIABLE_PATTERN
        String result =
            VARIABLE_PATTERN.matcher(templates).replaceAll((match) -> {
              String varName = match.group(1);
              if (vars.containsKey(varName)) {
                return vars.get(varName);
              }
              return match.group(0);
            });
        // Write the result to the response
        res.addBody(result);
      } catch (IOException e) {
        res.setResponseCode(Code.E500_INTERNAL_ERROR);
        e.printStackTrace();
      }
    };
  }
}
