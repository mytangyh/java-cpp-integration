plugins {
    java
    `java-library`
    `maven-publish`
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// 确定操作系统和架构
val currentOS = org.gradle.internal.os.OperatingSystem.current()
val platform = when {
    currentOS.isWindows -> "windows"
    currentOS.isLinux -> "linux"
    currentOS.isMacOsX -> "macos"
    else -> error("Unsupported operating system: ${currentOS.name}")
}

val arch = System.getProperty("os.arch").let {
    when {
        it.contains("amd64") || it.contains("x86_64") -> "x86_64"
        it.contains("arm64") || it.contains("aarch64") -> "arm64"
        it.contains("x86") -> "x86"
        else -> error("Unsupported architecture: $it")
    }
}

val nativeLibPrefix = if (currentOS.isWindows) "" else "lib"
val nativeLibName = "nativelibrary"
val nativeLibExt = when {
    currentOS.isWindows -> "dll"
    currentOS.isMacOsX -> "dylib"
    else -> "so"
}
val nativeLibFilename = "${nativeLibPrefix}${nativeLibName}.${nativeLibExt}"

// 生成JNI头文件的任务
tasks.register<Exec>("generateJniHeaders") {
    dependsOn("compileJava")

    val outputDir = layout.buildDirectory.dir("generated/jni").get().asFile
    outputDir.mkdirs()

    // 编译后的Java类路径
    val classesDir = layout.buildDirectory.dir("classes/java/main").get().asFile

    // 生成JNI头文件的命令
    commandLine(
        "javac",
        "-h", outputDir.absolutePath,
        "-classpath", classesDir.absolutePath,
        "${projectDir}/src/main/java/com/example/NativeLibrary.java"
    )
}

// 构建本地库的任务
tasks.register<Exec>("buildNativeLibrary") {
    dependsOn("generateJniHeaders")

    val buildDir = layout.buildDirectory.dir("native/$platform-$arch").get().asFile
    buildDir.mkdirs()

    workingDir = buildDir
    commandLine("cmake", "-DCMAKE_BUILD_TYPE=Release",
        "-DJNI_HEADERS_DIR=${layout.buildDirectory.dir("generated/jni").get().asFile.absolutePath}",
        "-DPLATFORM=$platform",
        "-DARCH=$arch",
        "${projectDir}/src/main/cpp")

    doLast {
        exec {
            workingDir = buildDir
            commandLine("cmake", "--build", ".", "--config", "Release")
        }
    }
}

// 创建一个任务来复制本地库到资源目录
tasks.register<Copy>("copyNativeLibrary") {
    dependsOn("buildNativeLibrary")

    from(layout.buildDirectory.dir("native/$platform-$arch/Release/$nativeLibFilename").orElse(
        layout.buildDirectory.dir("native/$platform-$arch/$nativeLibFilename")))

    // 创建与Java代码中期望的资源路径一致的目录结构
    into(layout.buildDirectory.dir("resources/main/native/$platform-$arch"))
}

// 让处理资源的任务依赖于复制本地库的任务
tasks.named("processResources") {
    dependsOn("copyNativeLibrary")
}

// 添加任务来创建测试运行时的本地库路径
tasks.register<Copy>("prepareTestNativeLibrary") {
    dependsOn("buildNativeLibrary")

    from(layout.buildDirectory.dir("native/$platform-$arch/Release/$nativeLibFilename").orElse(
        layout.buildDirectory.dir("native/$platform-$arch/$nativeLibFilename")))

    into(layout.buildDirectory.dir("resources/test/native/$platform-$arch"))
}

// 使测试任务依赖于准备测试本地库的任务
tasks.named("processTestResources") {
    dependsOn("prepareTestNativeLibrary")
}

