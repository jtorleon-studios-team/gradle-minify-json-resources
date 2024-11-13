package com.github.tmslpm.gradle.minify;

import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinifyJsonTaskTest {
  @TempDir
  public File rootProjectDir;
  public File fileSettingsGradle;
  public File fileBuildGradle;
  public File fileGradleProperties;

  @BeforeEach
  public void setup() {
    // Fake settings.gradle
    var settingsGradleFileName = "settings.gradle";
    fileSettingsGradle = new File(rootProjectDir, settingsGradleFileName);

    // Fake build.gradle
    var buildGradleFileName = "build.gradle";
    fileBuildGradle = new File(rootProjectDir, buildGradleFileName);

    // Fake gradle.properties
    var gradlePropertiesFileName = "gradle.properties";
    fileGradleProperties = new File(rootProjectDir, gradlePropertiesFileName);
  }

  @Test
  public void testSetup() {
    assertEquals(TaskOutcome.SUCCESS, Optional.ofNullable(GradleRunner.create()
            .withProjectDir(rootProjectDir)
            .withArguments("help")
            .withPluginClasspath()
            .build().task(":" + "help"))
        .map(BuildTask::getOutcome)
        .orElse(TaskOutcome.FAILED));
  }

  @Test
  public void testPluginIsAppliedAndTaskIsRegistered() throws IOException {
    // Create a temporary build file
    Path buildFile = rootProjectDir.toPath().resolve("build.gradle");
    Files.write(buildFile, String.format("""
            plugins { id '%s' }
        """, Main.PLUGIN_IDENTIFIER).getBytes());

    assertEquals(TaskOutcome.SUCCESS, Optional.ofNullable(GradleRunner.create()
            .withProjectDir(rootProjectDir)
            .withArguments(MinifyJsonTask.NAME)
            .withPluginClasspath()
            .build().task(":" + MinifyJsonTask.NAME))
        .map(BuildTask::getOutcome)
        .orElse(TaskOutcome.FAILED));
  }

}