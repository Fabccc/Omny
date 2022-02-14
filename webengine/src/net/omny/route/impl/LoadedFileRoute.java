package net.omny.route.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import net.omny.route.Request;
import net.omny.route.Response;
import net.omny.utils.Ex;
import net.omny.views.View;

public class LoadedFileRoute extends FileRoute {

  private byte[] bytes;

  public LoadedFileRoute(File file) {
    super(file);
    this.bytes = Ex.grab(() -> Files.readAllBytes(file.toPath()));
  }

  public LoadedFileRoute(String file) {
    super(file);
    this.bytes = Ex.grab(() -> Files.readAllBytes(Path.of(file)));
  }

  @Override
  public View handle(Request req, Response res) {
    super.handle(req, res);
    return res_ -> {
      res_.addBody(bytes);
    };
  }

}
