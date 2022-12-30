package com.discovery.thunderapp;

public class MemOpUtils {

    static {
        System.loadLibrary("MemFillTool");
    }

    public static native int malloc(int size);
    public static native int free();

}