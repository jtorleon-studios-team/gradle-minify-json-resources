# Gradle Plugin : Minify Json Resources

Minify Json Resources is a Gradle plugin that allows you to **minify JSON files** in your project.

- **License:** MIT
- **Author:** JTorleon Studios Team
- **Public Maven Repository:** https://jtorleon-studios-team-github-io.pages.dev/

## Installation

Add the public Maven repository in your `settings.gradle`:

```groovy
pluginManagement {
  repositories {
    maven {
      url = 'https://jtorleon-studios-team-github-io.pages.dev/'
    }
  }
}

```

Then apply the plugin in your `build.gradle`:


```groovy
plugin {
  id 'gradle-minify-json-resources' version '1.0.0'
}
```
