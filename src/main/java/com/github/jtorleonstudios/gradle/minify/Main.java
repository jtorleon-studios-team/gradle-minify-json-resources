package com.github.jtorleonstudios.gradle.minify;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.TaskContainer;

import java.util.Map;

public class Main implements Plugin<Object> {
  public static final String PLUGIN_IDENTIFIER = "gradle-minify-json-resources";

  @Override
  public void apply(Object target) {
    if (target instanceof Project project) {
      this.applyFrom(project);
    } else {
      Logging.getLogger(Main.class).error(
          String.format(
              "Failed to apply plugin '%s': Unsupported target type '%s'. "
              +"This plugin can only be applied to a Gradle Project (build.gradle).",
              PLUGIN_IDENTIFIER,
              target.getClass().getName()
          )
      );
    }
  }

  private void applyFrom(Project project) {
    project.apply(Map.of("plugin", "java"));
    TaskContainer tasks = project.getTasks();
    var ref = tasks.register(MinifyJsonTask.NAME, MinifyJsonTask.class);
    tasks.named("processResources").configure(v -> v.finalizedBy(ref));
  }
}
