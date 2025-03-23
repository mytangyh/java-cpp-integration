plugins {
    application
}

application {
    mainClass.set("com.example.DemoApplication")
}

dependencies {
    implementation(project(":jni-library"))
}

tasks.withType<JavaExec> {
    val os = org.gradle.internal.os.OperatingSystem.current()
    val libDir = when {
        os.isWindows -> "native/windows"
        os.isMacOsX -> "native/macos"
        os.isLinux -> "native/linux"
        else -> throw GradleException("Unsupported OS: ${os.name}")
    }
    systemProperty("java.library.path", "$buildDir/resources/main/$libDir")
}
