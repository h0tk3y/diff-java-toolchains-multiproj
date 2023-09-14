if (System.getProperty("idea.sync.active") == "true") {
    plugins.withId("java") {
        with(the<JavaPluginExtension>()) {
            sourceSets.named("main") {
                for (configurationName in listOf(
                    compileClasspathConfigurationName,
                    runtimeClasspathConfigurationName
                )) {
                    configurations.named(configurationName) {
                        attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 17)
                    }
                }
            }
        }
    }
}