halite
===

[![Maven Central](https://img.shields.io/maven-central/v/com.io7m.halite/com.io7m.halite.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.io7m.halite%22)
[![Maven Central (snapshot)](https://img.shields.io/nexus/s/https/s01.oss.sonatype.org/com.io7m.halite/com.io7m.halite.svg?style=flat-square)](https://s01.oss.sonatype.org/content/repositories/snapshots/com/io7m/halite/)
[![Codecov](https://img.shields.io/codecov/c/github/io7m/halite.svg?style=flat-square)](https://codecov.io/gh/io7m/halite)

![halite](./src/site/resources/halite.jpg?raw=true)

| JVM | Platform | Status |
|-----|----------|--------|
| OpenJDK (Temurin) Current | Linux | [![Build (OpenJDK (Temurin) Current, Linux)](https://img.shields.io/github/actions/workflow/status/io7m/halite/workflows/main.linux.temurin.current.yml)](https://github.com/io7m/halite/actions?query=workflow%3Amain.linux.temurin.current)|
| OpenJDK (Temurin) LTS | Linux | [![Build (OpenJDK (Temurin) LTS, Linux)](https://img.shields.io/github/actions/workflow/status/io7m/halite/workflows/main.linux.temurin.lts.yml)](https://github.com/io7m/halite/actions?query=workflow%3Amain.linux.temurin.lts)|
| OpenJDK (Temurin) Current | Windows | [![Build (OpenJDK (Temurin) Current, Windows)](https://img.shields.io/github/actions/workflow/status/io7m/halite/workflows/main.windows.temurin.current.yml)](https://github.com/io7m/halite/actions?query=workflow%3Amain.windows.temurin.current)|
| OpenJDK (Temurin) LTS | Windows | [![Build (OpenJDK (Temurin) LTS, Windows)](https://img.shields.io/github/actions/workflow/status/io7m/halite/workflows/main.windows.temurin.lts.yml)](https://github.com/io7m/halite/actions?query=workflow%3Amain.windows.temurin.lts)|


## Usage

The plugin is designed to be used on the command line without any
POM configuration. The plugin currently contains a single goal: `crystallize`.
Due to limitations in Maven, the `crystallize` goal MUST be executed after
the Maven `package` phase, in the same execution:

```
$ mvn package com.io7m.halite:com.io7m.halite.maven.plugin:0.0.1:crystallize
```

The `crystallize` goal copies all of the compile and runtime dependencies
and artifacts of the current project to a specified directory. The `outputDirectory`
parameter specifies the output directory:

```
$ mvn package com.io7m.halite:com.io7m.halite.maven.plugin:0.0.1:crystallize \
  -Dhalite.outputDirectory=/tmp/crystallized
...
[INFO] --- com.io7m.halite.maven.plugin:0.0.1:crystallize (default-cli) @ com.io7m.jregions ---
[INFO] included: com.io7m.jregions:com.io7m.jregions:pom:2.1.0-SNAPSHOT → included unconditionally
[INFO] included: com.io7m.jregions:com.io7m.jregions:pom:2.1.0-SNAPSHOT → included unconditionally
[INFO] copy com.io7m.jregions:com.io7m.jregions:pom:2.1.0-SNAPSHOT
[INFO] mkdir /tmp/crystallized
[INFO] mkdir /tmp/crystallized/com.io7m.jregions
[INFO] artifact com.io7m.jregions:com.io7m.jregions:pom:2.1.0-SNAPSHOT has no file
...
[INFO] --- com.io7m.halite.maven.plugin:0.0.1:crystallize (default-cli) @ com.io7m.jregions.generators ---
[INFO] included: com.io7m.jregions:com.io7m.jregions.generators:jar:2.1.0-SNAPSHOT → included unconditionally
[INFO] included: com.io7m.jregions:com.io7m.jregions.generators:test-jar:tests:2.1.0-SNAPSHOT → included unconditionally
[INFO] included: com.io7m.jregions:com.io7m.jregions.generators:jar:2.1.0-SNAPSHOT → included unconditionally
[INFO] included: com.io7m.jregions:com.io7m.jregions.core:jar:2.1.0-SNAPSHOT → included unconditionally
[INFO] included: com.io7m.junreachable:com.io7m.junreachable.core:jar:2.1.1 → included unconditionally
[INFO] included: com.io7m.jaffirm:com.io7m.jaffirm.core:jar:2.0.0 → included unconditionally
[INFO] included: net.java.quickcheck:quickcheck:jar:0.6 → included unconditionally
[INFO] included: net.java.quickcheck:quickcheck-src-generator:jar:0.6 → included unconditionally
[INFO] copy com.io7m.jregions:com.io7m.jregions.generators:jar:2.1.0-SNAPSHOT
[INFO] copy com.io7m.jregions:com.io7m.jregions.generators:test-jar:tests:2.1.0-SNAPSHOT
[INFO] copy com.io7m.jregions:com.io7m.jregions.core:jar:2.1.0-SNAPSHOT
[INFO] copy com.io7m.junreachable:com.io7m.junreachable.core:jar:2.1.1
[INFO] copy com.io7m.jaffirm:com.io7m.jaffirm.core:jar:2.0.0
[INFO] copy net.java.quickcheck:quickcheck:jar:0.6
[INFO] copy net.java.quickcheck:quickcheck-src-generator:jar:0.6
[INFO] mkdir /tmp/crystallized
[INFO] mkdir /tmp/crystallized/com.io7m.jregions
[INFO] copy /build/git/com.github/io7m/jregions/com.io7m.jregions.generators/target/com.io7m.jregions.generators-2.1.0-SNAPSHOT.jar /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.generators-2.1.0-SNAPSHOT.jar.tmp
[INFO] rename /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.generators-2.1.0-SNAPSHOT.jar.tmp /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.generators-2.1.0-SNAPSHOT.jar
[INFO] mkdir /tmp/crystallized/com.io7m.jregions
[INFO] copy /build/git/com.github/io7m/jregions/com.io7m.jregions.generators/target/com.io7m.jregions.generators-2.1.0-SNAPSHOT-tests.jar /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.generators-2.1.0-SNAPSHOT-tests.jar.tmp
[INFO] rename /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.generators-2.1.0-SNAPSHOT-tests.jar.tmp /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.generators-2.1.0-SNAPSHOT-tests.jar
[INFO] mkdir /tmp/crystallized/com.io7m.jregions
[INFO] copy /build/git/com.github/io7m/jregions/com.io7m.jregions.core/target/com.io7m.jregions.core-2.1.0-SNAPSHOT.jar /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.core-2.1.0-SNAPSHOT.jar.tmp
[INFO] rename /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.core-2.1.0-SNAPSHOT.jar.tmp /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.core-2.1.0-SNAPSHOT.jar
[INFO] mkdir /tmp/crystallized/com.io7m.junreachable
[INFO] copy /build/.m2/repository/com/io7m/junreachable/com.io7m.junreachable.core/2.1.1/com.io7m.junreachable.core-2.1.1.jar /tmp/crystallized/com.io7m.junreachable/com.io7m.junreachable.core-2.1.1.jar.tmp
[INFO] rename /tmp/crystallized/com.io7m.junreachable/com.io7m.junreachable.core-2.1.1.jar.tmp /tmp/crystallized/com.io7m.junreachable/com.io7m.junreachable.core-2.1.1.jar
[INFO] mkdir /tmp/crystallized/com.io7m.jaffirm
[INFO] copy /build/.m2/repository/com/io7m/jaffirm/com.io7m.jaffirm.core/2.0.0/com.io7m.jaffirm.core-2.0.0.jar /tmp/crystallized/com.io7m.jaffirm/com.io7m.jaffirm.core-2.0.0.jar.tmp
[INFO] rename /tmp/crystallized/com.io7m.jaffirm/com.io7m.jaffirm.core-2.0.0.jar.tmp /tmp/crystallized/com.io7m.jaffirm/com.io7m.jaffirm.core-2.0.0.jar
[INFO] mkdir /tmp/crystallized/net.java.quickcheck
[INFO] copy /build/.m2/repository/net/java/quickcheck/quickcheck/0.6/quickcheck-0.6.jar /tmp/crystallized/net.java.quickcheck/quickcheck-0.6.jar.tmp
[INFO] rename /tmp/crystallized/net.java.quickcheck/quickcheck-0.6.jar.tmp /tmp/crystallized/net.java.quickcheck/quickcheck-0.6.jar
[INFO] mkdir /tmp/crystallized/net.java.quickcheck
[INFO] copy /build/.m2/repository/net/java/quickcheck/quickcheck-src-generator/0.6/quickcheck-src-generator-0.6.jar /tmp/crystallized/net.java.quickcheck/quickcheck-src-generator-0.6.jar.tmp
[INFO] rename /tmp/crystallized/net.java.quickcheck/quickcheck-src-generator-0.6.jar.tmp /tmp/crystallized/net.java.quickcheck/quickcheck-src-generator-0.6.jar
...
[INFO] included: com.io7m.jregions:com.io7m.jregions.documentation:jar:2.1.0-SNAPSHOT → included unconditionally
[INFO] included: com.io7m.jregions:com.io7m.jregions.documentation:test-jar:tests:2.1.0-SNAPSHOT → included unconditionally
[INFO] excluded: org.immutables:value:jar:2.6.1 → scope is provided
[INFO] excluded: com.io7m.immutables.style:com.io7m.immutables.style:jar:0.0.1 → scope is provided
[INFO] included: com.io7m.jregions:com.io7m.jregions.documentation:jar:2.1.0-SNAPSHOT → included unconditionally
[INFO] included: com.io7m.jregions:com.io7m.jregions.core:jar:2.1.0-SNAPSHOT → included unconditionally
[INFO] included: com.io7m.junreachable:com.io7m.junreachable.core:jar:2.1.1 → included unconditionally
[INFO] included: com.io7m.jaffirm:com.io7m.jaffirm.core:jar:2.0.0 → included unconditionally
[INFO] included: com.io7m.jregions:com.io7m.jregions.generators:jar:2.1.0-SNAPSHOT → included unconditionally
[INFO] included: net.java.quickcheck:quickcheck:jar:0.6 → included unconditionally
[INFO] included: net.java.quickcheck:quickcheck-src-generator:jar:0.6 → included unconditionally
[INFO] copy com.io7m.jregions:com.io7m.jregions.documentation:jar:2.1.0-SNAPSHOT
[INFO] copy com.io7m.jregions:com.io7m.jregions.documentation:test-jar:tests:2.1.0-SNAPSHOT
[INFO] copy com.io7m.jregions:com.io7m.jregions.core:jar:2.1.0-SNAPSHOT
[INFO] copy com.io7m.junreachable:com.io7m.junreachable.core:jar:2.1.1
[INFO] copy com.io7m.jaffirm:com.io7m.jaffirm.core:jar:2.0.0
[INFO] copy com.io7m.jregions:com.io7m.jregions.generators:jar:2.1.0-SNAPSHOT
[INFO] copy net.java.quickcheck:quickcheck:jar:0.6
[INFO] copy net.java.quickcheck:quickcheck-src-generator:jar:0.6
[INFO] mkdir /tmp/crystallized
[INFO] mkdir /tmp/crystallized/com.io7m.jregions
[INFO] copy /build/git/com.github/io7m/jregions/com.io7m.jregions.documentation/target/com.io7m.jregions.documentation-2.1.0-SNAPSHOT.jar /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.documentation-2.1.0-SNAPSHOT.jar.tmp
[INFO] rename /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.documentation-2.1.0-SNAPSHOT.jar.tmp /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.documentation-2.1.0-SNAPSHOT.jar
[INFO] mkdir /tmp/crystallized/com.io7m.jregions
[INFO] copy /build/git/com.github/io7m/jregions/com.io7m.jregions.documentation/target/com.io7m.jregions.documentation-2.1.0-SNAPSHOT-tests.jar /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.documentation-2.1.0-SNAPSHOT-tests.jar.tmp
[INFO] rename /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.documentation-2.1.0-SNAPSHOT-tests.jar.tmp /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.documentation-2.1.0-SNAPSHOT-tests.jar
[INFO] mkdir /tmp/crystallized/com.io7m.jregions
[INFO] copy /build/git/com.github/io7m/jregions/com.io7m.jregions.core/target/com.io7m.jregions.core-2.1.0-SNAPSHOT.jar /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.core-2.1.0-SNAPSHOT.jar.tmp
[INFO] rename /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.core-2.1.0-SNAPSHOT.jar.tmp /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.core-2.1.0-SNAPSHOT.jar
[INFO] mkdir /tmp/crystallized/com.io7m.junreachable
[INFO] copy /build/.m2/repository/com/io7m/junreachable/com.io7m.junreachable.core/2.1.1/com.io7m.junreachable.core-2.1.1.jar /tmp/crystallized/com.io7m.junreachable/com.io7m.junreachable.core-2.1.1.jar.tmp
[INFO] rename /tmp/crystallized/com.io7m.junreachable/com.io7m.junreachable.core-2.1.1.jar.tmp /tmp/crystallized/com.io7m.junreachable/com.io7m.junreachable.core-2.1.1.jar
[INFO] mkdir /tmp/crystallized/com.io7m.jaffirm
[INFO] copy /build/.m2/repository/com/io7m/jaffirm/com.io7m.jaffirm.core/2.0.0/com.io7m.jaffirm.core-2.0.0.jar /tmp/crystallized/com.io7m.jaffirm/com.io7m.jaffirm.core-2.0.0.jar.tmp
[INFO] rename /tmp/crystallized/com.io7m.jaffirm/com.io7m.jaffirm.core-2.0.0.jar.tmp /tmp/crystallized/com.io7m.jaffirm/com.io7m.jaffirm.core-2.0.0.jar
[INFO] mkdir /tmp/crystallized/com.io7m.jregions
[INFO] copy /build/git/com.github/io7m/jregions/com.io7m.jregions.generators/target/com.io7m.jregions.generators-2.1.0-SNAPSHOT.jar /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.generators-2.1.0-SNAPSHOT.jar.tmp
[INFO] rename /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.generators-2.1.0-SNAPSHOT.jar.tmp /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.generators-2.1.0-SNAPSHOT.jar
[INFO] mkdir /tmp/crystallized/net.java.quickcheck
[INFO] copy /build/.m2/repository/net/java/quickcheck/quickcheck/0.6/quickcheck-0.6.jar /tmp/crystallized/net.java.quickcheck/quickcheck-0.6.jar.tmp
[INFO] rename /tmp/crystallized/net.java.quickcheck/quickcheck-0.6.jar.tmp /tmp/crystallized/net.java.quickcheck/quickcheck-0.6.jar
[INFO] mkdir /tmp/crystallized/net.java.quickcheck
[INFO] copy /build/.m2/repository/net/java/quickcheck/quickcheck-src-generator/0.6/quickcheck-src-generator-0.6.jar /tmp/crystallized/net.java.quickcheck/quickcheck-src-generator-0.6.jar.tmp
[INFO] rename /tmp/crystallized/net.java.quickcheck/quickcheck-src-generator-0.6.jar.tmp /tmp/crystallized/net.java.quickcheck/quickcheck-src-generator-0.6.jar
...
[INFO] included: com.io7m.jregions:com.io7m.jregions.tests:jar:2.1.0-SNAPSHOT → included unconditionally
[INFO] included: com.io7m.jregions:com.io7m.jregions.tests:test-jar:tests:2.1.0-SNAPSHOT → included unconditionally
[INFO] included: com.io7m.jregions:com.io7m.jregions.tests:jar:2.1.0-SNAPSHOT → included unconditionally
[INFO] included: com.io7m.jregions:com.io7m.jregions.core:jar:2.1.0-SNAPSHOT → included unconditionally
[INFO] included: com.io7m.jregions:com.io7m.jregions.generators:jar:2.1.0-SNAPSHOT → included unconditionally
[INFO] included: org.slf4j:slf4j-api:jar:1.8.0-beta2 → included unconditionally
[INFO] included: ch.qos.logback:logback-classic:jar:1.3.0-alpha4 → included unconditionally
[INFO] included: ch.qos.logback:logback-core:jar:1.3.0-alpha4 → included unconditionally
[INFO] included: com.sun.mail:javax.mail:jar:1.6.0 → included unconditionally
[INFO] included: javax.activation:activation:jar:1.1 → included unconditionally
[INFO] included: com.io7m.junreachable:com.io7m.junreachable.core:jar:2.1.1 → included unconditionally
[INFO] included: com.io7m.jaffirm:com.io7m.jaffirm.core:jar:2.0.0 → included unconditionally
[INFO] included: net.java.quickcheck:quickcheck:jar:0.6 → included unconditionally
[INFO] included: net.java.quickcheck:quickcheck-src-generator:jar:0.6 → included unconditionally
[INFO] included: junit:junit:jar:4.12 → included unconditionally
[INFO] included: org.hamcrest:hamcrest-core:jar:1.3 → included unconditionally
[INFO] copy com.io7m.jregions:com.io7m.jregions.tests:jar:2.1.0-SNAPSHOT
[INFO] copy com.io7m.jregions:com.io7m.jregions.tests:test-jar:tests:2.1.0-SNAPSHOT
[INFO] copy com.io7m.jregions:com.io7m.jregions.core:jar:2.1.0-SNAPSHOT
[INFO] copy com.io7m.jregions:com.io7m.jregions.generators:jar:2.1.0-SNAPSHOT
[INFO] copy org.slf4j:slf4j-api:jar:1.8.0-beta2
[INFO] copy ch.qos.logback:logback-classic:jar:1.3.0-alpha4
[INFO] copy ch.qos.logback:logback-core:jar:1.3.0-alpha4
[INFO] copy com.sun.mail:javax.mail:jar:1.6.0
[INFO] copy javax.activation:activation:jar:1.1
[INFO] copy com.io7m.junreachable:com.io7m.junreachable.core:jar:2.1.1
[INFO] copy com.io7m.jaffirm:com.io7m.jaffirm.core:jar:2.0.0
[INFO] copy net.java.quickcheck:quickcheck:jar:0.6
[INFO] copy net.java.quickcheck:quickcheck-src-generator:jar:0.6
[INFO] copy junit:junit:jar:4.12
[INFO] copy org.hamcrest:hamcrest-core:jar:1.3
[INFO] mkdir /tmp/crystallized
[INFO] mkdir /tmp/crystallized/com.io7m.jregions
[INFO] copy /build/git/com.github/io7m/jregions/com.io7m.jregions.tests/target/com.io7m.jregions.tests-2.1.0-SNAPSHOT.jar /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.tests-2.1.0-SNAPSHOT.jar.tmp
[INFO] rename /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.tests-2.1.0-SNAPSHOT.jar.tmp /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.tests-2.1.0-SNAPSHOT.jar
[INFO] mkdir /tmp/crystallized/com.io7m.jregions
[INFO] copy /build/git/com.github/io7m/jregions/com.io7m.jregions.tests/target/com.io7m.jregions.tests-2.1.0-SNAPSHOT-tests.jar /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.tests-2.1.0-SNAPSHOT-tests.jar.tmp
[INFO] rename /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.tests-2.1.0-SNAPSHOT-tests.jar.tmp /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.tests-2.1.0-SNAPSHOT-tests.jar
[INFO] mkdir /tmp/crystallized/com.io7m.jregions
[INFO] copy /build/git/com.github/io7m/jregions/com.io7m.jregions.core/target/com.io7m.jregions.core-2.1.0-SNAPSHOT.jar /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.core-2.1.0-SNAPSHOT.jar.tmp
[INFO] rename /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.core-2.1.0-SNAPSHOT.jar.tmp /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.core-2.1.0-SNAPSHOT.jar
[INFO] mkdir /tmp/crystallized/com.io7m.jregions
[INFO] copy /build/git/com.github/io7m/jregions/com.io7m.jregions.generators/target/com.io7m.jregions.generators-2.1.0-SNAPSHOT.jar /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.generators-2.1.0-SNAPSHOT.jar.tmp
[INFO] rename /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.generators-2.1.0-SNAPSHOT.jar.tmp /tmp/crystallized/com.io7m.jregions/com.io7m.jregions.generators-2.1.0-SNAPSHOT.jar
[INFO] mkdir /tmp/crystallized/org.slf4j
[INFO] copy /build/.m2/repository/org/slf4j/slf4j-api/1.8.0-beta2/slf4j-api-1.8.0-beta2.jar /tmp/crystallized/org.slf4j/slf4j-api-1.8.0-beta2.jar.tmp
[INFO] rename /tmp/crystallized/org.slf4j/slf4j-api-1.8.0-beta2.jar.tmp /tmp/crystallized/org.slf4j/slf4j-api-1.8.0-beta2.jar
[INFO] mkdir /tmp/crystallized/ch.qos.logback
[INFO] copy /build/.m2/repository/ch/qos/logback/logback-classic/1.3.0-alpha4/logback-classic-1.3.0-alpha4.jar /tmp/crystallized/ch.qos.logback/logback-classic-1.3.0-alpha4.jar.tmp
[INFO] rename /tmp/crystallized/ch.qos.logback/logback-classic-1.3.0-alpha4.jar.tmp /tmp/crystallized/ch.qos.logback/logback-classic-1.3.0-alpha4.jar
[INFO] mkdir /tmp/crystallized/ch.qos.logback
[INFO] copy /build/.m2/repository/ch/qos/logback/logback-core/1.3.0-alpha4/logback-core-1.3.0-alpha4.jar /tmp/crystallized/ch.qos.logback/logback-core-1.3.0-alpha4.jar.tmp
[INFO] rename /tmp/crystallized/ch.qos.logback/logback-core-1.3.0-alpha4.jar.tmp /tmp/crystallized/ch.qos.logback/logback-core-1.3.0-alpha4.jar
[INFO] mkdir /tmp/crystallized/com.sun.mail
[INFO] copy /build/.m2/repository/com/sun/mail/javax.mail/1.6.0/javax.mail-1.6.0.jar /tmp/crystallized/com.sun.mail/javax.mail-1.6.0.jar.tmp
[INFO] rename /tmp/crystallized/com.sun.mail/javax.mail-1.6.0.jar.tmp /tmp/crystallized/com.sun.mail/javax.mail-1.6.0.jar
[INFO] mkdir /tmp/crystallized/javax.activation
[INFO] copy /build/.m2/repository/javax/activation/activation/1.1/activation-1.1.jar /tmp/crystallized/javax.activation/activation-1.1.jar.tmp
[INFO] rename /tmp/crystallized/javax.activation/activation-1.1.jar.tmp /tmp/crystallized/javax.activation/activation-1.1.jar
[INFO] mkdir /tmp/crystallized/com.io7m.junreachable
[INFO] copy /build/.m2/repository/com/io7m/junreachable/com.io7m.junreachable.core/2.1.1/com.io7m.junreachable.core-2.1.1.jar /tmp/crystallized/com.io7m.junreachable/com.io7m.junreachable.core-2.1.1.jar.tmp
[INFO] rename /tmp/crystallized/com.io7m.junreachable/com.io7m.junreachable.core-2.1.1.jar.tmp /tmp/crystallized/com.io7m.junreachable/com.io7m.junreachable.core-2.1.1.jar
[INFO] mkdir /tmp/crystallized/com.io7m.jaffirm
[INFO] copy /build/.m2/repository/com/io7m/jaffirm/com.io7m.jaffirm.core/2.0.0/com.io7m.jaffirm.core-2.0.0.jar /tmp/crystallized/com.io7m.jaffirm/com.io7m.jaffirm.core-2.0.0.jar.tmp
[INFO] rename /tmp/crystallized/com.io7m.jaffirm/com.io7m.jaffirm.core-2.0.0.jar.tmp /tmp/crystallized/com.io7m.jaffirm/com.io7m.jaffirm.core-2.0.0.jar
[INFO] mkdir /tmp/crystallized/net.java.quickcheck
[INFO] copy /build/.m2/repository/net/java/quickcheck/quickcheck/0.6/quickcheck-0.6.jar /tmp/crystallized/net.java.quickcheck/quickcheck-0.6.jar.tmp
[INFO] rename /tmp/crystallized/net.java.quickcheck/quickcheck-0.6.jar.tmp /tmp/crystallized/net.java.quickcheck/quickcheck-0.6.jar
[INFO] mkdir /tmp/crystallized/net.java.quickcheck
[INFO] copy /build/.m2/repository/net/java/quickcheck/quickcheck-src-generator/0.6/quickcheck-src-generator-0.6.jar /tmp/crystallized/net.java.quickcheck/quickcheck-src-generator-0.6.jar.tmp
[INFO] rename /tmp/crystallized/net.java.quickcheck/quickcheck-src-generator-0.6.jar.tmp /tmp/crystallized/net.java.quickcheck/quickcheck-src-generator-0.6.jar
[INFO] mkdir /tmp/crystallized/junit
[INFO] copy /build/.m2/repository/junit/junit/4.12/junit-4.12.jar /tmp/crystallized/junit/junit-4.12.jar.tmp
[INFO] rename /tmp/crystallized/junit/junit-4.12.jar.tmp /tmp/crystallized/junit/junit-4.12.jar
[INFO] mkdir /tmp/crystallized/org.hamcrest
[INFO] copy /build/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar /tmp/crystallized/org.hamcrest/hamcrest-core-1.3.jar.tmp
[INFO] rename /tmp/crystallized/org.hamcrest/hamcrest-core-1.3.jar.tmp /tmp/crystallized/org.hamcrest/hamcrest-core-1.3.jar

$ find /tmp/crystallized/ -type f
/tmp/crystallized/org.hamcrest/hamcrest-core-1.3.jar
/tmp/crystallized/junit/junit-4.12.jar
/tmp/crystallized/javax.activation/activation-1.1.jar
/tmp/crystallized/com.sun.mail/javax.mail-1.6.0.jar
/tmp/crystallized/ch.qos.logback/logback-core-1.3.0-alpha4.jar
/tmp/crystallized/ch.qos.logback/logback-classic-1.3.0-alpha4.jar
/tmp/crystallized/org.slf4j/slf4j-api-1.8.0-beta2.jar
/tmp/crystallized/net.java.quickcheck/quickcheck-src-generator-0.6.jar
/tmp/crystallized/net.java.quickcheck/quickcheck-0.6.jar
/tmp/crystallized/com.io7m.jaffirm/com.io7m.jaffirm.core-2.0.0.jar
/tmp/crystallized/com.io7m.junreachable/com.io7m.junreachable.core-2.1.1.jar
/tmp/crystallized/com.io7m.jregions/com.io7m.jregions.generators-2.1.0-SNAPSHOT.jar
/tmp/crystallized/com.io7m.jregions/com.io7m.jregions.core-2.1.0-SNAPSHOT.jar
/tmp/crystallized/com.io7m.jregions/com.io7m.jregions.tests-2.1.0-SNAPSHOT-tests.jar
/tmp/crystallized/com.io7m.jregions/com.io7m.jregions.tests-2.1.0-SNAPSHOT.jar
/tmp/crystallized/com.io7m.jregions/com.io7m.jregions.documentation-2.1.0-SNAPSHOT-tests.jar
/tmp/crystallized/com.io7m.jregions/com.io7m.jregions.documentation-2.1.0-SNAPSHOT.jar
/tmp/crystallized/com.io7m.jregions/com.io7m.jregions.generators-2.1.0-SNAPSHOT-tests.jar
/tmp/crystallized/com.io7m.jregions/com.io7m.jregions.core-2.1.0-SNAPSHOT-tests.jar
```

## Filtering

Sometimes it is desirable to filter the dependencies/artifacts that will be copied. The
plugin supports a flexible inclusion/exclusion mechanism to achieve this. The plugin
can accept _filter files_ against which the _full identifiers_ of each artifact will
be tested. A _filter file_ is a file containing one [java.util.Pattern](https://docs.oracle.com/javase/10/docs/api/java/util/regex/Pattern.html) per line. Lines beginning with the `#` character are ignored.

More formally, if `C` is the set of artifacts that will be copied by the plugin,
`I` is the set of artifacts to be _included_, and `X` is the set of artifacts
to be _excluded_ then, for any given artifact `a`, `a ∈ C iff a ∈ I ∧ a ∉ X`.

For example, an _inclusion_ filter file containing the following:

```
ch\.qos\.logback:logback-classic:jar:.*
```

Will result in only artifacts of type `jar`, with a group
`ch.qos.logback` artifact `logback-classic`, and of any version,
being copied. Filter files are evaluated from top to bottom, and
the first pattern that matches will halt evaluation and cause the
artifact being tested to be included into the set of artifacts that
will be copied. The `halite.inclusionListFile` parameter specifies
an _inclusion_ filter file. If no inclusion filter file is specified,
a default filter is used that effectively includes all artifacts.

The `halite.exclusionListFile` parameter specifies an _exclusion_
filter file that can be used to exclude artifacts. The syntax of
an _exclusion_ filter file is identical to that of the _inclusion_
filter file. If a pattern in the _exclusion_ filter file matches an
artifact, then that artifact will be excluded from copying.

As an example:

```
$ cat inclusion.txt
# Include all of jregions
com\.io7m\.jregions:.*
# Include the SLF4J API
org\.slf4j:.*

$ cat exclusion.txt
# Do not copy documentation
com\.io7m\.jregions:com\.io7m\.jregions\.documentation:.*

$ mvn package com.io7m.halite:com.io7m.halite.maven.plugin:0.0.1:crystallize \
  -Dhalite.outputDirectory=/tmp/crystallized \
  -Dhalite.inclusionListFile=inclusion.txt \
  -Dhalite.exclusionListFile=exclusion.txt

$ find /tmp/crystallized/ -type f
/tmp/crystallized/org.slf4j/slf4j-api-1.8.0-beta2.jar
/tmp/crystallized/com.io7m.jregions/com.io7m.jregions.generators-2.1.0-SNAPSHOT.jar
/tmp/crystallized/com.io7m.jregions/com.io7m.jregions.core-2.1.0-SNAPSHOT.jar
/tmp/crystallized/com.io7m.jregions/com.io7m.jregions.tests-2.1.0-SNAPSHOT-tests.jar
/tmp/crystallized/com.io7m.jregions/com.io7m.jregions.tests-2.1.0-SNAPSHOT.jar
/tmp/crystallized/com.io7m.jregions/com.io7m.jregions.generators-2.1.0-SNAPSHOT-tests.jar
/tmp/crystallized/com.io7m.jregions/com.io7m.jregions.core-2.1.0-SNAPSHOT-tests.jar
```

Note that artifacts in the `com.io7m.jregions` and `org.slf4j` groups were _included_,
and the `com.io7m.jregions.documentation` artifact was specifically _excluded_.

Finally, the `halite.scopes` parameter can be used to specify a comma separated list
of other _scopes_ (such as `test` or `provided` scope dependencies) from which to
include dependencies. If no `halite.scopes` parameter is specified, the plugin behaves
as if the user had specified `compile,runtime`.

## Copying

The `halite.logCopies` parameter specifies a file to which the names of artifacts
that will be copied will be written. This can be used if, for some reason, you
only want to know what will be copied but want some other system to handle the
actual copying. Using the example above:

```
$ mvn package com.io7m.halite:com.io7m.halite.maven.plugin:0.0.1:crystallize \
  -Dhalite.outputDirectory=/tmp/crystallized \
  -Dhalite.inclusionListFile=inclusion.txt \
  -Dhalite.exclusionListFile=exclusion.txt \
  -Dhalite.logCopies=/tmp/copies.txt

$ cat /tmp/copies.txt
copy com.io7m.jregions:com.io7m.jregions.core:jar:2.1.0-SNAPSHOT /build/git/com.github/io7m/jregions/com.io7m.jregions.core/target/com.io7m.jregions.core-2.1.0-SNAPSHOT.jar
copy com.io7m.jregions:com.io7m.jregions.core:test-jar:tests:2.1.0-SNAPSHOT /build/git/com.github/io7m/jregions/com.io7m.jregions.core/target/com.io7m.jregions.core-2.1.0-SNAPSHOT-tests.jar
copy com.io7m.jregions:com.io7m.jregions.generators:jar:2.1.0-SNAPSHOT /build/git/com.github/io7m/jregions/com.io7m.jregions.generators/target/com.io7m.jregions.generators-2.1.0-SNAPSHOT.jar
copy com.io7m.jregions:com.io7m.jregions.generators:test-jar:tests:2.1.0-SNAPSHOT /build/git/com.github/io7m/jregions/com.io7m.jregions.generators/target/com.io7m.jregions.generators-2.1.0-SNAPSHOT-tests.jar
copy com.io7m.jregions:com.io7m.jregions.core:jar:2.1.0-SNAPSHOT /build/git/com.github/io7m/jregions/com.io7m.jregions.core/target/com.io7m.jregions.core-2.1.0-SNAPSHOT.jar
copy com.io7m.jregions:com.io7m.jregions.tests:jar:2.1.0-SNAPSHOT /build/git/com.github/io7m/jregions/com.io7m.jregions.tests/target/com.io7m.jregions.tests-2.1.0-SNAPSHOT.jar
copy com.io7m.jregions:com.io7m.jregions.tests:test-jar:tests:2.1.0-SNAPSHOT /build/git/com.github/io7m/jregions/com.io7m.jregions.tests/target/com.io7m.jregions.tests-2.1.0-SNAPSHOT-tests.jar
copy com.io7m.jregions:com.io7m.jregions.core:jar:2.1.0-SNAPSHOT /build/git/com.github/io7m/jregions/com.io7m.jregions.core/target/com.io7m.jregions.core-2.1.0-SNAPSHOT.jar
copy com.io7m.jregions:com.io7m.jregions.generators:jar:2.1.0-SNAPSHOT /build/git/com.github/io7m/jregions/com.io7m.jregions.generators/target/com.io7m.jregions.generators-2.1.0-SNAPSHOT.jar
copy org.slf4j:slf4j-api:jar:1.8.0-beta2 /build/.m2/repository/org/slf4j/slf4j-api/1.8.0-beta2/slf4j-api-1.8.0-beta2.jar
```

The first field is always `copy`, the second field is the artifact
identifier, and the third field is the absolute path to the artifact
on disk.

Finally the `halite.dryRun` parameter can be used to disable the
actual copying of artifacts and/or creation of directories.


