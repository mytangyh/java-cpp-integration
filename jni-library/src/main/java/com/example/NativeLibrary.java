package com.example;

public class NativeLibrary {
    static {
        System.loadLibrary("nativelibrary"); // 由调用方（demo）决定路径
    }

    public native int add(int a, int b);
    public native String processString(String input);
}