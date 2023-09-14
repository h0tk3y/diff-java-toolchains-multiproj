plugins {
 `java-library`
 `consume-java-downgrade`
 kotlin("jvm")
}

java {
 toolchain {
  languageVersion.set(JavaLanguageVersion.of(11))
 }
}

dependencies {
 implementation(project(":project-z"))
}