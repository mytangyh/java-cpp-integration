#ifndef NATIVE_LIBRARY_H
#define NATIVE_LIBRARY_H

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

// JNI function declarations
JNIEXPORT jint JNICALL Java_com_example_NativeLibrary_add
  (JNIEnv *, jobject, jint, jint);

JNIEXPORT jstring JNICALL Java_com_example_NativeLibrary_processString
  (JNIEnv *, jobject, jstring);

#ifdef __cplusplus
}
#endif

#endif // NATIVE_LIBRARY_H