plugins {
    `java`
    `java-library`
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.test {
    useJUnitPlatform()
}

sourceSets {
    main {
        resources {
            srcDirs("src/main/resources", "build/natives/lib")
        }
    }
}

tasks.compileJava {
    options.compilerArgs.addAll(listOf("-h", file("src/main/include").absolutePath))
}

tasks.register<Exec>("compileJNI") {
    dependsOn("compileJava")

    val os = org.gradle.internal.os.OperatingSystem.current()
    val buildDir = project.layout.buildDirectory.dir("natives").get().asFile
    val sourceDir = project.projectDir.absolutePath.replace("\\", "/") // Windows 兼容路径
    val buildPath = buildDir.absolutePath.replace("\\", "/") // Windows 兼容路径

    val command = if (os.isWindows) {
        listOf("cmd", "/c",
            "if not exist \"$buildPath\" mkdir \"$buildPath\" && cd \"$buildPath\" && cmake \"$sourceDir\" && cmake --build ."
        )
    } else {
        listOf("sh", "-c",
            "mkdir -p \"$buildPath\" && cd \"$buildPath\" && cmake \"$sourceDir\" && make"
        )
    }

    commandLine(command)
}


tasks.clean {
    doFirst {
        delete(fileTree("src/main/include") {
            include("jni_*.h")
        })
    }
}

tasks.processResources {
    dependsOn(tasks.named("compileJNI"))
}

tasks.withType<Test> {
    systemProperty("java.library.path", "build/natives/lib")
    testLogging.showStandardStreams = false
}