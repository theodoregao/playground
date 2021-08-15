package com.playground;

import com.playground.utils.FileUtils;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        System.out.println("main()");
        for (File file: FileUtils.getNonLocalDiskPath()) {
            System.out.println("Find non local disk path: " + file.getAbsolutePath() + "/" + file.getName());
        }
    }

}