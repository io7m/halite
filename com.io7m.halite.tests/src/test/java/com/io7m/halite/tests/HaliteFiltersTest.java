package com.io7m.halite.tests;

import com.io7m.halite.maven.plugin.HaliteFilters;
import com.io7m.halite.maven.plugin.HaliteReason;
import io.vavr.collection.Vector;
import org.apache.maven.artifact.Artifact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.regex.Pattern;

public final class HaliteFiltersTest
{
  @Test
  public void testIncludesEmptyNone()
  {
    final Artifact artifact = Mockito.mock(Artifact.class);

    Mockito.when(artifact.getId())
      .thenReturn("a:b:c:d");

    Assertions.assertFalse(
      HaliteFilters.inclusionOfPatterns(Vector.empty())
        .includes(artifact)
        .isPresent());
  }

  @Test
  public void testIncludesPattern()
  {
    final Artifact artifact = Mockito.mock(Artifact.class);

    Mockito.when(artifact.getId())
      .thenReturn("a:b:c:d");

    Assertions.assertEquals(
      Optional.of(HaliteReason.of("a:b:c:d", "matched by pattern .*")),
      HaliteFilters.inclusionOfPatterns(Vector.of(Pattern.compile(".*")))
        .includes(artifact));
  }

  @Test
  public void testIncludesAll()
  {
    final Artifact artifact = Mockito.mock(Artifact.class);

    Mockito.when(artifact.getId())
      .thenReturn("a:b:c:d");

    Assertions.assertEquals(
      Optional.of(HaliteReason.of("a:b:c:d", "included unconditionally")),
      HaliteFilters.includeAll()
        .includes(artifact));
  }

  @Test
  public void testExcludesPattern()
  {
    final Artifact artifact = Mockito.mock(Artifact.class);

    Mockito.when(artifact.getId())
      .thenReturn("a:b:c:d");

    Assertions.assertEquals(
      Optional.of(HaliteReason.of("a:b:c:d", "matched by pattern .*")),
      HaliteFilters.exclusionOfPatterns(Vector.of(Pattern.compile(".*")))
        .excludes(artifact));
  }

  @Test
  public void testExcludesNothing()
  {
    final Artifact artifact = Mockito.mock(Artifact.class);

    Mockito.when(artifact.getId())
      .thenReturn("a:b:c:d");

    Assertions.assertEquals(
      Optional.empty(),
      HaliteFilters.excludeNone()
        .excludes(artifact));
  }

  @Test
  public void testExcludesAll()
  {
    final Artifact artifact = Mockito.mock(Artifact.class);

    Mockito.when(artifact.getId())
      .thenReturn("a:b:c:d");

    Assertions.assertEquals(
      Optional.of(HaliteReason.of("a:b:c:d", "excluded unconditionally")),
      HaliteFilters.excludeAll()
        .excludes(artifact));
  }

  @Test
  public void testExcludesEmptyNone()
  {
    final Artifact artifact = Mockito.mock(Artifact.class);

    Mockito.when(artifact.getId())
      .thenReturn("a:b:c:d");

    Assertions.assertFalse(
      HaliteFilters.exclusionOfPatterns(Vector.empty())
        .excludes(artifact)
        .isPresent());
  }
}
