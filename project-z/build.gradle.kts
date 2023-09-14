plugins {
 `java-library`
 `recompile-with-java-downgrade`
 kotlin("jvm")
}

javaDowngrade {
 toVersion.set(11)
}

java {
 toolchain {
  languageVersion.set(JavaLanguageVersion.of(17))
 }
}