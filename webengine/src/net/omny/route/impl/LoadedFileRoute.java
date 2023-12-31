package net.omny.route.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.Getter;
import net.omny.route.Request;
import net.omny.route.Response;
import net.omny.utils.Ex;
import net.omny.utils.HTTPUtils;
import net.omny.utils.HTTPUtils.Headers;
import net.omny.views.View;

@Getter
public class LoadedFileRoute extends FileRoute {

  private byte[] bytes;
  private View v;
  private String mimeType;

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

  void init() {
    this.v = res_ -> {
      if (mimeType.equals("application/pdf")) {
        res_.setBinary(true);
      }
      if (mimeType.equals("application/x-msdownload")) {
        res_.setBinary(true);
      }
      res_.setHeader(Headers.CONTENT_TYPE, mimeType);

      res_.addBody(this.bytes);
    };

    try {
      this.mimeType = Files.probeContentType(Path.of(this.filePath));
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (mimeType == null) {
      mimeType = HTTPUtils.findMime(this.filePath);
    }

  }

  @Override
  public View handle(Request req, Response res) {
    super.handle(req, res);
    return this.v;
  }

  @Override
  public String toString() {
    return "{" +
        " bytes='" + getBytes() + "'" +
        ", v='" + getV() + "'" +
        ", mimeType='" + getMimeType() + "'" +
        ", filePath='" + getFilePath() + "'" +
        ", fileView='" + getFileView() + "'" +
        "}";
  }

}
