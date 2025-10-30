package com.github.jtorleonstudios.gradle.minify;

import groovy.json.JsonOutput;
import groovy.json.JsonParserType;
import groovy.json.JsonSlurper;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskContainer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;

public abstract class MinifyJsonTask extends DefaultTask {
  public final static String NAME = "minifyJsonTask";

  @TaskAction
  public void run() {
    FileCollection files = getProject()
        .fileTree(getProject().getBuildDir())
        .matching(v -> v.include("**/*.json"));

    if (files.isEmpty()) {
      this.getLogger().info("minification successful, 0 byte saved on 0 file");
      return; // do nothing
    }

    JsonSlurper jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
    long totalDiff = 0;
    for (File v : files) {
      try {
        String minifiedJson = JsonOutput.toJson(jsonSlurper.parse(v));
        totalDiff += this.debug(v, minifiedJson);
        Files.writeString(v.toPath(), minifiedJson);
      } catch (Exception e) {
        throw new GradleException("Failed to minify JSON file: " + v, e);
      }
    }

    this.getLogger().info("minification successful, {} bytes saved on {} files",
        totalDiff,
        files.getFiles().size()
    );
  }

  private long debug(@NotNull File entry, @NotNull String result) {
    String[] paths = entry.getPath().replace("\\", "/").split("build");
    long diff = entry.length() - result.length();
    this.getLogger().debug("minify json: diff {} bytes <- {}\n",
        entry.length() - result.length(),
        paths.length > 0 ? ".." + paths[1] : entry.getPath()
    );
    return diff;
  }

}
