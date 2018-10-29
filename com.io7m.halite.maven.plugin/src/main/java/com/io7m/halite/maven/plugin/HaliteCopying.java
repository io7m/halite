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
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.BiFunction;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Functions to copy artifacts.
 */

public final class HaliteCopying
{
  private HaliteCopying()
  {

  }

  private static Exception accumulateException(
    final Exception exception,
    final Exception next)
  {
    if (exception == null) {
      return next;
    }
    exception.addSuppressed(next);
    return exception;
  }

  /**
   * Copy all artifacts to the given directory.
   *
   * @param log         A log interface
   * @param output_path The output directory
   * @param artifacts   The list of artifacts
   * @param dry_run     {@code true} if no actual I/O should take place
   * @param exceptions  A supplier of exceptions
   * @param <E>         The type of thrown exceptions
   *
   * @throws E On errors
   */

  public static <E extends Exception> void copyArtifacts(
    final Log log,
    final Path output_path,
    final List<Artifact> artifacts,
    final BiFunction<String, Exception, E> exceptions,
    final boolean dry_run)
    throws E
  {
    Objects.requireNonNull(log, "log");
    Objects.requireNonNull(output_path, "output_path");
    Objects.requireNonNull(artifacts, "artifacts");
    Objects.requireNonNull(exceptions, "exceptions");

    log.info("mkdir " + output_path);

    try {
      if (!dry_run) {
        Files.createDirectories(output_path);
      }
    } catch (final IOException e) {
      throw exceptions.apply("Could not create directory: " + output_path, e);
    }

    Exception exception = null;
    for (final Artifact artifact : artifacts) {
      final Path group_path =
        output_path.resolve(artifact.getGroupId())
          .toAbsolutePath();

      try {
        log.info("mkdir " + group_path);

        if (!dry_run) {
          Files.createDirectories(group_path);
        }
      } catch (final Exception e) {
        exception = accumulateException(exception, e);
      }

      final File file = artifact.getFile();
      if (file == null) {
        log.info("artifact " + artifact.getId() + " has no file");
        continue;
      }

      final Path input_path = file.toPath().toAbsolutePath();
      final String input_name = file.getName();
      final Path output_file = group_path.resolve(input_name).toAbsolutePath();
      final Path output_tmp = group_path.resolve(input_name + ".tmp").toAbsolutePath();

      try {
        log.info("copy " + input_path + " " + output_tmp);
        log.info("rename " + output_tmp + " " + output_file);

        if (!dry_run) {
          Files.copy(input_path, output_tmp, REPLACE_EXISTING);
          Files.move(output_tmp, output_file, ATOMIC_MOVE, REPLACE_EXISTING);
        }
      } catch (final Exception e) {
        exception = accumulateException(exception, e);
      }
    }

    if (exception != null) {
      throw exceptions.apply(exception.getMessage(), exception);
    }
  }
}
