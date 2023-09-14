plugins {
 `java-library`
 kotlin("jvm")
}

java {
 toolchain {
  languageVersion.set(JavaLanguageVersion.of(17))
 }
}

dependencies {
 implementation(project(":project-z"))
}