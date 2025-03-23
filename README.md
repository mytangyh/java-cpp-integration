# Java-C++ Integration

This project demonstrates how to integrate Java and C++ code using JNI (Java Native Interface), allowing Java applications to call native C++ functions.

## Project Overview

This project provides a framework for:
- Calling C++ code from Java applications
- Passing data between Java and C++
- Automatically building native libraries for multiple platforms
- Loading native libraries at runtime

## Prerequisites

- JDK 11 or later
- CMake 3.10 or later
- C++ compiler (GCC, Clang, or MSVC)
- Gradle 7.0 or later

## Building the Project

```bash
# Clone the repository
git clone https://github.com/yourusername/java-cpp-integration.git
cd java-cpp-integration

# Build the project
./gradlew build
```

The build process will:
1. Compile Java code
2. Generate JNI headers
3. Build the native library for your platform
4. Package everything into a JAR file

## Running the Demo

```bash
./gradlew run
```

## Project Structure

```
java-cpp-integration/
├── src/
│   ├── main/
│   │   ├── cpp/                 # C++ native code
│   │   │   ├── NativeLibrary.cpp
│   │   │   ├── NativeLibrary.h
│   │   │   └── CMakeLists.txt
│   │   └── java/
│   │       └── com/
│   │           └── example/
│   │               ├── Main.java          # Demo application
│   │               └── NativeLibrary.java # JNI wrapper
├── build.gradle.kts              # Gradle build script
└── README.md                     # This file
```

## How It Works

1. The Java `NativeLibrary` class defines native methods that will be implemented in C++
2. Gradle generates JNI headers during the build process
3. C++ implementation provides the actual functionality
4. The native library is automatically extracted and loaded at runtime

## Extending the Project

To add new native methods:

1. Add new native method declarations in `NativeLibrary.java`
2. Run `./gradlew generateJniHeaders` to create updated header files
3. Implement the new methods in `NativeLibrary.cpp`
4. Rebuild the project

## Cross-Platform Support

The project automatically detects the current operating system and architecture, and builds the appropriate native library. Supported platforms:

- Windows (x86, x86_64)
- macOS (x86_64, arm64)
- Linux (x86_64, arm64)

## License

[Your license information here]
