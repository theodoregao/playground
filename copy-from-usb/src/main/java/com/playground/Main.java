package com.playground;

import com.playground.utils.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final List<String> EXTENSITIONS = new ArrayList<>();

    static {
        EXTENSITIONS.add("txt");
        EXTENSITIONS.add("png");
        EXTENSITIONS.add("jpg");
    }

    public static void main(String[] arg) {
        List<File> files = FileUtils.getNonLocalDiskPath();
        File toFolder = new File("build/copy");
        if (!toFolder.exists()) {
            toFolder.mkdirs();
        }

        for (File file : files) {
            System.out.println("Find non local disk path: " + file.getAbsolutePath() + "/" + file.getName());
            copy(file, toFolder);
        }
    }

    private static void copy(File file, File toFolder) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                copy(child, toFolder);
            }
        }
        for (String ext : EXTENSITIONS) {
            if (file.getName().endsWith(ext)) {
                try {
                    FileInputStream fromFile = new FileInputStream(file);
                    FileOutputStream toFile = new FileOutputStream(new File(toFolder, file.getName()));
                    final int BUFFER_SIZE = 1024;
                    final byte[] buffer = new byte[BUFFER_SIZE];
                    int byteRead = 0;
                    while ((byteRead = fromFile.read(buffer)) > 0) {
                        toFile.write(buffer, 0, byteRead);
                    }

                    System.out.println("copied file from " + file.getName() + "to" + toFolder.getName());
                    fromFile.close();
                    toFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}