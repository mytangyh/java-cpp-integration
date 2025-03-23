#include "NativeLibrary.h"
#include <string>
#include <cstring>

// Implementation of the add method
JNIEXPORT jint JNICALL Java_com_example_NativeLibrary_add
  (JNIEnv *env, jobject obj, jint a, jint b) {
    return a + b;
}

// Implementation of the processString method
JNIEXPORT jstring JNICALL Java_com_example_NativeLibrary_processString
  (JNIEnv *env, jobject obj, jstring input) {
    // Convert Java string to C++ string
    const char *inputStr = env->GetStringUTFChars(input, nullptr);
    if (inputStr == nullptr) {
        return nullptr; // OutOfMemoryError already thrown
    }

    // Process the string (in this case, prepend a message)
    std::string result = "C++ received: ";
    result += inputStr;

    // Release the string
    env->ReleaseStringUTFChars(input, inputStr);

    // Return the new string as a jstring
    return env->NewStringUTF(result.c_str());
}