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
import io.vavr.collection.Vector;
import io.vavr.control.Validation;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Functions to load patterns from files.
 */

public final class HalitePatternListFile
{
  private HalitePatternListFile()
  {

  }

  /**
   * Read a list of patterns from the given file.
   *
   * @param path The pattern file
   *
   * @return A list of patterns, or a list of parse errors
   *
   * @throws IOException On I/O errors
   */

  public static Validation<Seq<String>, Seq<Pattern>> ofFile(
    final Path path)
    throws IOException
  {
    Objects.requireNonNull(path, "path");

    Vector<String> errors = Vector.empty();
    Vector<Pattern> patterns = Vector.empty();

    try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
      int line_number = 1;
      while (true) {
        final String line = reader.readLine();
        if (line == null) {
          break;
        }

        if (!line.isEmpty()) {
          if (line.codePointAt(0) == (int) '#') {
            continue;
          }
        }

        try {
          patterns = patterns.append(Pattern.compile(line));
        } catch (final PatternSyntaxException e) {
          errors = errors.append(
            new StringBuilder(128)
              .append(path)
              .append(":")
              .append(line_number)
              .append(":")
              .append(e.getIndex())
              .append(": ")
              .append(line)
              .append(" - ")
              .append(e.getMessage())
              .toString());
        }

        ++line_number;
      }
    }

    if (errors.isEmpty()) {
      return Validation.valid(patterns);
    }
    return Validation.invalid(errors);
  }
}
