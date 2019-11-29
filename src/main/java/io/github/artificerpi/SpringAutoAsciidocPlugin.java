/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package io.github.artificerpi;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.asciidoctor.gradle.AsciidoctorPlugin;
import org.asciidoctor.gradle.AsciidoctorTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.DependencyResolutionListener;
import org.gradle.api.artifacts.ResolvableDependencies;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.javadoc.Javadoc;
import org.gradle.external.javadoc.JavadocMemberLevel;

/**
 * Spring Rest Docs Auto plugin
 */
public class SpringAutoAsciidocPlugin implements Plugin<Project> {
  private static final String JSONDOCLET_CONFIG_NAME = "jsondoclet";
  private static final String SPRING_AUTO_RESTDOCS_CORE_DEPENDENCY = "capital.scalable:spring-auto-restdocs-core:";
  private static final String SPRING_AUTO_RESTDOCS_JSON_DOCLET_DEPENDENCY = "capital.scalable:spring-auto-restdocs-json-doclet:";

  @Override
  public void apply(Project project) {

    File snippetsDir = new File(project.getBuildDir(), "generated-snippets");
    File javadocJsonDir = new File(project.getBuildDir(), "generated-javadoc-json");

    project.getPluginManager().apply(AsciidoctorPlugin.class);
    project.getConfigurations().create(JSONDOCLET_CONFIG_NAME);

    configureDependencies(project);

    Task testTask = project.getTasks().getByName("test");
    testTask.dependsOn("jsonDoclet");
    testTask.getActions().add(e -> {
      System.setProperty("org.springframework.restdocs.outputDir", snippetsDir.getAbsolutePath());
      System.setProperty("org.springframework.restdocs.javadocJsonDir", javadocJsonDir.getAbsolutePath());
    });

    registerJsonDocletTask(project);
    registerAsciidocTemplateTask(project);

    AsciidoctorTask asciidoctor = (AsciidoctorTask) project.getTasks().getByName("asciidoctor");
    asciidoctor.dependsOn(testTask);
    asciidoctor.dependsOn(project.getTasks().getByName("asciidocTemplate"));
    asciidoctor.setSourceDir(new File(project.getBuildDir(), "docs/asciidoc"));
    asciidoctor.setOutputDir(new File(project.getBuildDir(), "generated-docs"));

    Map<String, String> optionMap = new HashMap<>();
    optionMap.put("backend", "html");
    optionMap.put("doctype", "book");
    asciidoctor.options(optionMap);

    Map<String, String> attrMap = new HashMap<>();
    attrMap.put("source-highlighter", "highlightjs");
    attrMap.put("snippets", snippetsDir.getAbsolutePath());
    asciidoctor.attributes(attrMap);
    asciidoctor.doLast(task -> {
      try {
        new File(project.getBuildDir(), "resources/main/public").mkdirs();
        Files.copy(Paths.get(new File(project.getBuildDir(), "generated-docs/html5/index.html").getAbsolutePath()),
            Paths.get(new File(project.getBuildDir(), "resources/main/public/index.html").getAbsolutePath()),
            StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
  }

  private void configureDependencies(Project project) {
    final String version = "2.0.6";
    project.getGradle().addListener(new DependencyResolutionListener() {
      @Override
      public void beforeResolve(ResolvableDependencies resolvableDependencies) {
        project.getConfigurations().getByName("testCompile").getDependencies()
            .add(project.getDependencies().create(SPRING_AUTO_RESTDOCS_CORE_DEPENDENCY + version));
        project.getConfigurations().getByName(JSONDOCLET_CONFIG_NAME).getDependencies()
            .add(project.getDependencies().create(SPRING_AUTO_RESTDOCS_JSON_DOCLET_DEPENDENCY + version));
        project.getGradle().removeListener(this);
      }

      @Override
      public void afterResolve(ResolvableDependencies resolvableDependencies) {
      }
    });
  }

  private void registerJsonDocletTask(Project project) {
    final JavaPluginConvention javaPlugin = project.getConvention().getPlugin(JavaPluginConvention.class);
    final SourceSetContainer sourceSets = javaPlugin.getSourceSets();
    final SourceSet mainSourceSet = sourceSets.getByName("main");

    // https://docs.gradle.org/current/javadoc/org/gradle/api/tasks/javadoc/Javadoc.html
    project.getTasks().register("jsonDoclet", Javadoc.class, task -> {
      task.dependsOn(JavaPlugin.COMPILE_JAVA_TASK_NAME);
      task.setSource(mainSourceSet.getAllJava());
      task.setClasspath(mainSourceSet.getCompileClasspath());
      task.setDestinationDir(new File(project.getBuildDir(), "generated-javadoc-json"));
      task.options(option -> {
        option.setDoclet("capital.scalable.restdocs.jsondoclet.ExtractDocumentationAsJsonDoclet");
        option.setDocletpath(new ArrayList<>(project.getConfigurations().getByName(JSONDOCLET_CONFIG_NAME).getFiles()));
        option.setMemberLevel(JavadocMemberLevel.PACKAGE);
      });
    });
  }

  private void registerAsciidocTemplateTask(Project project) {
    project.getTasks().register("asciidocTemplate", task -> {

      task.dependsOn("test");

      AsciidoctorTask asciidoctor = (AsciidoctorTask) project.getTasks().getByName("asciidoctor");
      String regex = Objects.toString(asciidoctor.getAttributes().get("classNamePattern"),
          "(\\w+)-rest-controller-tests");
      Pattern p = Pattern.compile(regex);
      List<File> snippetDirList = new ArrayList<>();
      List<String> snippetNameList = new ArrayList<>();

      task.doFirst(e -> {
        File snippetsDir = new File(project.getBuildDir(), "generated-snippets");
        snippetsDir.mkdirs();

        File[] directories = snippetsDir.listFiles(new FilenameFilter() {
          @Override
          public boolean accept(File current, String name) {
            return name.matches(regex) && new File(current, name).isDirectory();
          }
        });

        snippetDirList.addAll(Arrays.asList(directories));
        snippetNameList.addAll(snippetDirList.stream().map(dir -> {
          Matcher m = p.matcher(dir.getName());
          if (m.matches()) {
            return m.group(1);
          } else {
            return dir.getName();
          }
        }).collect(Collectors.toList()));
      });

      task.doLast(e -> {
        project.copy(c -> {
          c.from("src/docs/asciidoc");
          c.into(new File(project.getBuildDir(), "docs/asciidoc"));
          // index and overview
          c.include("*.adoc");
          c.eachFile(it -> {
            Map<String, Object> m = new HashMap<>();
            m.put("snippetNameList", snippetNameList);
            if (it.getName().equals("index.adoc")) {
              it.expand(m);
            }
          });
        });

        IntStream.range(0, snippetNameList.size()).forEach(i -> {
          String snippetName = snippetNameList.get(i);
          List<String> snippetMethodNameList = Arrays.asList(snippetDirList.get(i).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
              return new File(current, name).isDirectory();
            }
          })).stream().map(file -> file.getName()).collect(Collectors.toList());

          project.copy(c2 -> {
            // FIXME
            c2.from("src/docs/asciidoc/snippet.adoc.template");
            c2.into(new File(project.getBuildDir(), "docs/asciidoc"));
            c2.rename(file -> snippetName + ".adoc");
            Map<String, Object> m = new HashMap<>();
            m.put("snippetClassName", snippetDirList.get(i).getName());
            m.put("snippetMethodNameList", snippetMethodNameList);
            c2.expand(m);
          });
        });
      });
    });
  }
}