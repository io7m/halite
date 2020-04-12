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

package com.io7m.halite.tests;

import com.io7m.halite.maven.plugin.HalitePatternListFile;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public final class HalitePatternListFileTest
{
  private static final Logger LOG =
    Logger.getLogger(HalitePatternListFileTest.class.getCanonicalName());

  @Test
  public void testEmptyFile()
    throws IOException
  {
    final Path path = Files.createTempFile("halite-", ".txt");

    final Validation<Seq<String>, Seq<Pattern>> result = HalitePatternListFile.ofFile(path);
    Assertions.assertTrue(result.isValid(), "Parsing succeeded");

    final Seq<Pattern> patterns = result.get();
    Assertions.assertEquals(0, patterns.size());
  }

  @Test
  public void testSimpleFile()
    throws IOException
  {
    final Path path = Files.createTempFile("halite-", ".txt");
    try (BufferedWriter writer = Files.newBufferedWriter(path)) {
      writer.append("a");
      writer.newLine();
      writer.append("[0-9]+");
      writer.newLine();
      writer.append(".*");
      writer.newLine();
    }

    final Validation<Seq<String>, Seq<Pattern>> result = HalitePatternListFile.ofFile(path);
    Assertions.assertTrue(result.isValid(), "Parsing succeeded");

    final Seq<Pattern> patterns = result.get();
    Assertions.assertEquals(3, patterns.size());
    Assertions.assertEquals("a", patterns.get(0).pattern());
    Assertions.assertEquals("[0-9]+", patterns.get(1).pattern());
    Assertions.assertEquals(".*", patterns.get(2).pattern());
  }

  @Test
  public void testSimpleFileCommented()
    throws IOException
  {
    final Path path = Files.createTempFile("halite-", ".txt");
    try (BufferedWriter writer = Files.newBufferedWriter(path)) {
      writer.append("# Nothing");
      writer.newLine();
      writer.append("a");
      writer.newLine();
      writer.append("[0-9]+");
      writer.newLine();
      writer.append(".*");
      writer.newLine();
    }

    final Validation<Seq<String>, Seq<Pattern>> result = HalitePatternListFile.ofFile(path);
    Assertions.assertTrue(result.isValid(), "Parsing succeeded");

    final Seq<Pattern> patterns = result.get();
    Assertions.assertEquals(3, patterns.size());
    Assertions.assertEquals("a", patterns.get(0).pattern());
    Assertions.assertEquals("[0-9]+", patterns.get(1).pattern());
    Assertions.assertEquals(".*", patterns.get(2).pattern());
  }

  @Test
  public void testAllPatternsFailed()
    throws IOException
  {
    final Path path = Files.createTempFile("halite-", ".txt");
    try (BufferedWriter writer = Files.newBufferedWriter(path)) {
      writer.append("[");
      writer.newLine();
      writer.append("(");
      writer.newLine();
      writer.append("[");
      writer.newLine();
      writer.append("(");
      writer.newLine();
    }

    final Validation<Seq<String>, Seq<Pattern>> result = HalitePatternListFile.ofFile(path);
    Assertions.assertTrue(result.isInvalid(), "Parsing failed");

    final Seq<String> errors = result.getError();
    errors.forEach(LOG::severe);

    Assertions.assertEquals(4, errors.size());
    Assertions.assertTrue(errors.get(0).contains("txt:1"));
    Assertions.assertTrue(errors.get(1).contains("txt:2"));
    Assertions.assertTrue(errors.get(2).contains("txt:3"));
    Assertions.assertTrue(errors.get(3).contains("txt:4"));
  }
}
