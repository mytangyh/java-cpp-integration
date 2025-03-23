plugins {
    // Kotlin DSL 插件，适用于 Gradle Kotlin 配置
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0" apply false

}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")

    group = "com.example"
    version = "1.0-SNAPSHOT"

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
