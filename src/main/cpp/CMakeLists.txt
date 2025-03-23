cmake_minimum_required(VERSION 3.10)
project(NativeLibrary)

# Set C++ standard
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# JNI headers directory passed from Gradle
set(JNI_HEADERS_DIR "" CACHE PATH "Directory containing generated JNI headers")

# Platform identifier passed from Gradle
set(PLATFORM "" CACHE STRING "Target platform (windows, linux, macos)")
set(ARCH "" CACHE STRING "Target architecture (x86_64, arm64, x86)")

# Find JNI package
find_package(JNI REQUIRED)

# Include directories
include_directories(
    ${JNI_INCLUDE_DIRS}
    ${JNI_HEADERS_DIR}
)

# Library name
set(LIBRARY_NAME "nativelibrary")

# Library type
if(PLATFORM STREQUAL "windows")
    add_library(${LIBRARY_NAME} SHARED NativeLibrary.cpp)
else()
    add_library(${LIBRARY_NAME} SHARED NativeLibrary.cpp)
endif()

# Platform-specific settings
if(PLATFORM STREQUAL "windows")
    # Windows-specific settings
    set_target_properties(${LIBRARY_NAME} PROPERTIES PREFIX "")
elseif(PLATFORM STREQUAL "macos")
    # macOS-specific settings
    set_target_properties(${LIBRARY_NAME} PROPERTIES
        SUFFIX ".dylib"
        INSTALL_NAME_DIR "@rpath"
        BUILD_WITH_INSTALL_RPATH TRUE
    )

    # For macOS universal binary (if needed)
    if(CMAKE_OSX_ARCHITECTURES)
        set_target_properties(${LIBRARY_NAME} PROPERTIES OSX_ARCHITECTURES "${CMAKE_OSX_ARCHITECTURES}")
    endif()
endif()

# Installation rules
install(TARGETS ${LIBRARY_NAME}
    LIBRARY DESTINATION lib
    RUNTIME DESTINATION bin
)