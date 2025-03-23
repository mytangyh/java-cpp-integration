package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class NativeLibrary {
    // Native methods declaration - must be marked as native
    public native int add(int a, int b);
    public native String processString(String input);

    // Static block to load the native library
    private static boolean libraryLoaded = false;

    public static synchronized void loadLibrary() {
        if (libraryLoaded) {
            return;
        }

        try {
            // Determine the OS and architecture
            String os = System.getProperty("os.name").toLowerCase();
            String arch = System.getProperty("os.arch").toLowerCase();

            // Map to standardized platform names
            String platform;
            if (os.contains("win")) {
                platform = "windows";
            } else if (os.contains("mac") || os.contains("darwin")) {
                platform = "macos";
            } else if (os.contains("linux")) {
                platform = "linux";
            } else {
                throw new UnsupportedOperationException("Unsupported operating system: " + os);
            }

            // Map to standardized architecture names
            String architecture;
            if (arch.contains("amd64") || arch.contains("x86_64")) {
                architecture = "x86_64";
            } else if (arch.contains("arm64") || arch.contains("aarch64")) {
                architecture = "arm64";
            } else if (arch.contains("x86")) {
                architecture = "x86";
            } else {
                throw new UnsupportedOperationException("Unsupported architecture: " + arch);
            }

            // Determine library file name based on platform
            String libPrefix = platform.equals("windows") ? "" : "lib";
            String libExtension;
            if (platform.equals("windows")) {
                libExtension = "dll";
            } else if (platform.equals("macos")) {
                libExtension = "dylib";
            } else {
                libExtension = "so";
            }

            String libraryName = libPrefix + "nativelibrary." + libExtension;

            // Path to the library in resources
            String resourcePath = "/native/" + platform + "-" + architecture + "/" + libraryName;

            // Create temp directory if it doesn't exist
            Path tempDir = Files.createTempDirectory("jni-native-lib");
            tempDir.toFile().deleteOnExit();

            // Extract library to temp directory
            Path tempLibPath = tempDir.resolve(libraryName);
            try (InputStream in = NativeLibrary.class.getResourceAsStream(resourcePath)) {
                if (in == null) {
                    throw new IOException("Native library not found: " + resourcePath);
                }
                Files.copy(in, tempLibPath, StandardCopyOption.REPLACE_EXISTING);
            }

            // Make the library executable on Unix systems
            if (!platform.equals("windows")) {
                tempLibPath.toFile().setExecutable(true);
            }

            // Load the library
            System.load(tempLibPath.toAbsolutePath().toString());
            libraryLoaded = true;

        } catch (IOException | UnsupportedOperationException e) {
            throw new RuntimeException("Failed to load native library", e);
        }
    }

    // Static initializer block - alternative to explicit loading
    static {
        try {
            // Will be called when class is loaded
            // Can uncomment this if you want automatic loading
            // loadLibrary();
        } catch (Exception e) {
            System.err.println("Failed to load native library: " + e.getMessage());
        }
    }

    // Constructor
    public NativeLibrary() {
        // Ensure library is loaded when an instance is created
        loadLibrary();
    }
}