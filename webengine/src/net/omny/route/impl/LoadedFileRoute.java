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
  private View v;

  public LoadedFileRoute(File file) {
    super(file);
    this.bytes = Ex.grab(() -> Files.readAllBytes(file.toPath()));
    this.init();
  }

  public LoadedFileRoute(String file) {
    super(file);
    this.bytes = Ex.grab(() -> Files.readAllBytes(Path.of(file)));
    this.init();
  }

  void init(){
    this.v = res_ -> {
      res_.addBody(bytes);
    };
  }

  @Override
  public View handle(Request req, Response res) {
    super.handle(req, res);
    return this.v;
  }

}
