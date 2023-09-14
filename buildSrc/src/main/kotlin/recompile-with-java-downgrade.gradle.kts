import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
}

abstract class JavaDowngradeExtension {
    abstract val toVersion: Property<Int>
}

val javaDowngradeExtension = extensions.create<JavaDowngradeExtension>("javaDowngrade")

if (System.getProperty("idea.sync.active") == "true") {
    afterEvaluate {
        java {
            toolchain.languageVersion.set(javaDowngradeExtension.toVersion.map(JavaLanguageVersion::of))
        }
    }
} else run {
    java {
        val main by sourceSets.existing
        val javaDowngrade by sourceSets.creating {
            tasks.withType<JavaCompile>().named(compileJavaTaskName).configure {
                setSource(main.flatMap { tasks.withType<JavaCompile>().named(it.compileJavaTaskName) }
                    .map { it.source })
                javaCompiler.set(javaToolchains.compilerFor {
                    languageVersion.set(javaDowngradeExtension.toVersion.map(JavaLanguageVersion::of))
                })
            }
        }

        plugins.withId("org.jetbrains.kotlin.jvm") {
            with(project.the<KotlinJvmProjectExtension>()) {
                val mainKotlin = target.compilations.named("main")
                target.compilations.named(javaDowngrade.name) {
                    compileTaskProvider {
                        this as KotlinCompile
                        setSource(mainKotlin.flatMap { it.compileTaskProvider }.map { (it as KotlinCompile).sources })
                        kotlinOptions {
                            this.jvmTarget = javaDowngradeExtension.toVersion.get().toString()
                        }
                    }
                }
            }
        }

        registerFeature("javaDowngrade") {
            usingSourceSet(javaDowngrade)
            // Make the feature variants expose the default capability, so they can be used as a replacement for the "normal" variants
            capability("${project.group}", project.name, "${project.version}")
        }
        with(javaDowngrade) {
            for (configurationName in listOf(apiElementsConfigurationName, runtimeElementsConfigurationName)) {
                configurations.named(configurationName) {
                    attributes.attributeProvider(
                        TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE,
                        javaDowngradeExtension.toVersion
                    )
                }
            }
        }
    }
}