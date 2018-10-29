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

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

/**
 * Functions to construct filters.
 */

public final class HaliteFilters
{
  private HaliteFilters()
  {

  }

  /**
   * Load an inclusion filter from the given optional path. If no path is given, return a filter
   * that includes everything.
   *
   * @param path       The file, if any
   * @param exceptions A function that accepts an error message and an exception, and returns an
   *                   {@code E}
   * @param <E>        The type of raised exceptions
   *
   * @return A filter, or a list of parse errors
   *
   * @throws E On I/O errors
   */

  public static <E extends Exception> HaliteInclusionFilterType inclusionsOrAny(
    final Optional<Path> path,
    final BiFunction<String, Exception, E> exceptions)
    throws E
  {
    Objects.requireNonNull(path, "path");
    Objects.requireNonNull(exceptions, "exceptions");

    try {
      if (path.isPresent()) {
        final Validation<Seq<String>, HaliteInclusionFilterType> result =
          inclusionsFromFile(path.get());

        if (result.isValid()) {
          return result.get();
        }
        throw new IOException("Unable to parse inclusion file");
      }
      return includeAll();
    } catch (final IOException e) {
      throw exceptions.apply("Unable to load inclusion filter", e);
    }
  }

  /**
   * Load an exclusion filter from the given optional path. If no path is given, return a filter
   * that excludes nothing.
   *
   * @param path       The file, if any
   * @param exceptions A function that accepts an error message and an exception, and returns an
   *                   {@code E}
   * @param <E>        The type of raised exceptions
   *
   * @return A filter, or a list of parse errors
   *
   * @throws E On I/O errors
   */

  public static <E extends Exception> HaliteExclusionFilterType exclusionsOrAny(
    final Optional<Path> path,
    final BiFunction<String, Exception, E> exceptions)
    throws E
  {
    Objects.requireNonNull(path, "path");
    Objects.requireNonNull(exceptions, "exceptions");

    try {
      if (path.isPresent()) {
        final Validation<Seq<String>, HaliteExclusionFilterType> result =
          exclusionsFromFile(path.get());

        if (result.isValid()) {
          return result.get();
        }
        throw new IOException("Unable to parse exclusion file");
      }
      return excludeNone();
    } catch (final IOException e) {
      throw exceptions.apply("Unable to load exclusion filter", e);
    }
  }

  /**
   * Load an inclusion filter from the given file.
   *
   * @param path The file
   *
   * @return A filter, or a list of parse errors
   *
   * @throws IOException On I/O errors
   */

  public static Validation<Seq<String>, HaliteInclusionFilterType> inclusionsFromFile(
    final Path path)
    throws IOException
  {
    Objects.requireNonNull(path, "path");
    return HalitePatternListFile.ofFile(path).map(HaliteFilters::inclusionOfPatterns);
  }

  /**
   * Load an exclusion filter from the given file.
   *
   * @param path The file
   *
   * @return A filter, or a list of parse errors
   *
   * @throws IOException On I/O errors
   */

  public static Validation<Seq<String>, HaliteExclusionFilterType> exclusionsFromFile(
    final Path path)
    throws IOException
  {
    Objects.requireNonNull(path, "path");
    return HalitePatternListFile.ofFile(path).map(HaliteFilters::exclusionOfPatterns);
  }

  /**
   * @return A filter that unconditionally includes everything
   */

  public static HaliteInclusionFilterType includeAll()
  {
    return artifact -> Optional.of(HaliteReason.of(artifact.getId(), "included unconditionally"));
  }

  /**
   * @return A filter that unconditionally excludes everything
   */

  public static HaliteExclusionFilterType excludeAll()
  {
    return artifact -> Optional.of(HaliteReason.of(artifact.getId(), "excluded unconditionally"));
  }

  /**
   * @return A filter that excludes nothing
   */

  public static HaliteExclusionFilterType excludeNone()
  {
    return artifact -> Optional.empty();
  }

  /**
   * @param patterns The list of patterns
   *
   * @return A filter that includes an artifact if any of the given patterns match
   */

  public static HaliteInclusionFilterType inclusionOfPatterns(
    final Seq<Pattern> patterns)
  {
    Objects.requireNonNull(patterns, "patterns");
    return artifact -> {
      final String id = artifact.getId();
      return patterns.filter(pattern -> pattern.matcher(id).matches())
        .map(pattern -> HaliteReason.of(id, "matched by pattern " + pattern.pattern()))
        .headOption()
        .toJavaOptional();
    };
  }

  /**
   * @param patterns The list of patterns
   *
   * @return A filter that excludes an artifact if any of the given patterns match
   */

  public static HaliteExclusionFilterType exclusionOfPatterns(
    final Seq<Pattern> patterns)
  {
    Objects.requireNonNull(patterns, "patterns");
    return artifact -> {
      final String id = artifact.getId();
      return patterns.filter(pattern -> pattern.matcher(id).matches())
        .map(pattern -> HaliteReason.of(id, "matched by pattern " + pattern.pattern()))
        .headOption()
        .toJavaOptional();
    };
  }
}
