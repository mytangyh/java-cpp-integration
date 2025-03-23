package com.example;

public class Main {
    public static void main(String[] args) {
        // Load the native library
        NativeLibrary.loadLibrary();

        // Create instance of our wrapper
        NativeLibrary lib = new NativeLibrary();

        // Test the add method
        int result = lib.add(5, 7);
        System.out.println("5 + 7 = " + result);

        // Test the string method
        String message = "Hello from Java";
        String response = lib.processString(message);
        System.out.println("Sent: " + message);
        System.out.println("Received: " + response);
    }
}