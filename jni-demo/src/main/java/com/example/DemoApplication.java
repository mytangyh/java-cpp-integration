package com.example;


public class DemoApplication {
    public static void main(String[] args) {
        NativeLibrary library = new NativeLibrary();
        int result = library.add(5, 3);
        System.out.println("5 + 3 = " + result);
    }
}