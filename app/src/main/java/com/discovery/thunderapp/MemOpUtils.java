package com.discovery.thunderapp;

/*
 * @author: Sasikumar Bharanikumar
 * @version: 1.0
 */

public class MemOpUtils {

    static {
        System.loadLibrary("MemFillTool");
    }

    public static native int malloc(int size);
    public static native int free();

}
