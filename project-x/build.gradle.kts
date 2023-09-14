plugins {
 `java-library`
}

java {
 toolchain {
  languageVersion.set(JavaLanguageVersion.of(11))
 }
}

dependencies {
 implementation(project(":project-z"))
}