/*
 * Copyright © 2018 Mark Raynsford <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.halite.maven.plugin;

import io.vavr.collection.List;
import io.vavr.collection.Vector;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.graph.DependencyNode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

/**
 * The basic copying mojo.
 */

@Mojo(
  name = "crystallize",
  defaultPhase = LifecyclePhase.PACKAGE)
public final class HaliteMojo extends AbstractMojo
{
  /**
   * Parameter to allow skipping of the operation.
   */

  @Parameter(
    name = "skip",
    property = "halite.skip",
    required = false)
  private boolean skip;

  /**
   * Access to the Maven project.
   */

  @Parameter(
    defaultValue = "${project}",
    required = true,
    readonly = true)
  private MavenProject project;

  /**
   * Access to the Maven session.
   */

  @Parameter(defaultValue = "${session}", readonly = true, required = true)
  private MavenSession session;

  /**
   * A file containing one inclusion pattern per line.
   */

  @Parameter(required = false, property = "halite.inclusionListFile")
  private String includeListFile;

  /**
   * A file containing one exclusion pattern per line.
   */

  @Parameter(required = false, property = "halite.exclusionListFile")
  private String excludeListFile;

  /**
   * A comma-separated list of scopes to be included.
   */

  @Parameter(required = false, property = "halite.scopes")
  private String scopes = "runtime,compile";

  /**
   * A file that will be used to log copy operations.
   */

  @Parameter(required = false, property = "halite.logCopies")
  private String logCopiesFile;

  /**
   * A file that will be used to log copy operations.
   */

  @Parameter(required = true, property = "halite.outputDirectory")
  private String outputDirectory;

  /**
   * True if copying should not be performed.
   */

  @Parameter(required = false, property = "halite.dryRun")
  private boolean dryRun = false;

  @Component
  private DependencyGraphBuilder dependencyGraphBuilder;

  /**
   * Construct the mojo.
   */

  public HaliteMojo()
  {

  }

  @Override
  public void execute()
    throws MojoExecutionException
  {
    final Log log = this.getLog();

    if (this.skip) {
      log.info("skipping execution");
      return;
    }

    final HaliteInclusionFilterType inclusion_filter =
      loadInclusionFilter(this.includeListFile);
    final HaliteExclusionFilterType exclusion_filter =
      loadExclusionFilter(this.excludeListFile);

    final List<Artifact> artifacts =
      this.collectAllArtifacts(log, inclusion_filter, exclusion_filter);

    logCopies(artifacts, this.logCopiesFile);

    this.runCopy(log, artifacts);
  }

  private void runCopy(
    final Log log,
    final List<Artifact> artifacts)
    throws MojoExecutionException
  {
    HaliteCopying.copyArtifacts(
      log,
      Paths.get(this.outputDirectory).toAbsolutePath(),
      artifacts,
      MojoExecutionException::new,
      this.dryRun);
  }

  private List<Artifact> collectAllArtifacts(
    final Log log,
    final HaliteInclusionFilterType inclusion_filter,
    final HaliteExclusionFilterType exclusion_filter)
    throws MojoExecutionException
  {
    List<Artifact> artifacts = List.empty();

    if (isDesirableArtifact(log, this.project.getArtifact(), inclusion_filter, exclusion_filter)) {
      artifacts = artifacts.append(this.project.getArtifact());
    }

    for (final Artifact artifact : this.project.getArtifacts()) {
      if (isDesirableArtifact(log, artifact, inclusion_filter, exclusion_filter)) {
        artifacts = artifacts.append(artifact);
      }
    }

    for (final Artifact artifact : this.project.getAttachedArtifacts()) {
      if (isDesirableArtifact(log, artifact, inclusion_filter, exclusion_filter)) {
        artifacts = artifacts.append(artifact);
      }
    }

    final ProjectBuildingRequest request =
      new DefaultProjectBuildingRequest(this.session.getProjectBuildingRequest());

    request.setProject(this.project);

    try {
      Objects.requireNonNull(request, "request");
      final Vector<String> filter_scopes = Vector.of(this.scopes.split(","));

      final DependencyNode node =
        this.dependencyGraphBuilder.buildDependencyGraph(request, artifact -> {
          final String artifact_scope = artifact.getScope();
          for (final String filter_scope : filter_scopes) {
            if (Objects.equals(artifact_scope, filter_scope)) {
              return true;
            }
          }
          log.info("excluded: " + artifact.getId() + " → scope is " + artifact_scope);
          return false;
        });

      artifacts = collectDependencies(log, artifacts, inclusion_filter, exclusion_filter, node);
    } catch (final DependencyGraphBuilderException e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }

    final List<Artifact> artifacts_unique = artifacts.distinctBy(Artifact::getId);
    for (final Artifact artifact : artifacts_unique) {
      log.info("copy " + artifact.getId());
    }

    return artifacts_unique;
  }

  private static void logCopies(
    final List<Artifact> artifacts,
    final String file)
    throws MojoExecutionException
  {
    if (file != null) {
      try (BufferedWriter writer =
             Files.newBufferedWriter(Paths.get(file), APPEND, CREATE)) {
        for (final Artifact artifact : artifacts) {
          if (artifact.getFile() == null) {
            continue;
          }

          writer.write("copy ");
          writer.write(artifact.getId());
          writer.write(" ");
          writer.write(artifact.getFile().toString());
          writer.newLine();
        }
        writer.flush();
      } catch (final IOException e) {
        throw new MojoExecutionException(e.getMessage(), e);
      }
    }
  }

  private static HaliteInclusionFilterType loadInclusionFilter(final String include_file)
    throws MojoExecutionException
  {
    return HaliteFilters.inclusionsOrAny(
      Optional.ofNullable(include_file).map(Paths::get),
      MojoExecutionException::new);
  }

  private static HaliteExclusionFilterType loadExclusionFilter(final String exclude_file)
    throws MojoExecutionException
  {
    return HaliteFilters.exclusionsOrAny(
      Optional.ofNullable(exclude_file).map(Paths::get),
      MojoExecutionException::new);
  }

  private static boolean isDesirableArtifact(
    final Log log,
    final Artifact artifact,
    final HaliteInclusionFilterType inclusion,
    final HaliteExclusionFilterType exclusion)
  {
    final String id = artifact.getId();

    final Optional<HaliteReason> included = inclusion.includes(artifact);
    if (included.isPresent()) {
      log.info("included: " + id + " → " + included.get().reason());

      final Optional<HaliteReason> excluded = exclusion.excludes(artifact);
      if (excluded.isPresent()) {
        log.info("excluded: " + id + " → " + excluded.get().reason());
        return false;
      }

      return true;
    }

    log.info("not included: " + id);
    return false;
  }

  private static List<Artifact> collectDependencies(
    final Log log,
    final List<Artifact> artifacts,
    final HaliteInclusionFilterType inclusion,
    final HaliteExclusionFilterType exclusion,
    final DependencyNode node)
  {
    List<Artifact> next_artifacts = artifacts;

    if (isDesirableArtifact(log, node.getArtifact(), inclusion, exclusion)) {
      next_artifacts = next_artifacts.append(node.getArtifact());

      for (final DependencyNode child : node.getChildren()) {
        next_artifacts = collectDependencies(log, next_artifacts, inclusion, exclusion, child);
      }
    }

    return next_artifacts;
  }
}
