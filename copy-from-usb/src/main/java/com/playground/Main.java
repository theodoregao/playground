package com.playground;

import com.playground.utils.FileUtils;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    private static final List<String> EXTENSITIONS = List.of("txt", "png", "jpg");
    private static final int BUFFER_SIZE = 1024;
    private static final Set<String> set = new HashSet<>();

    public static void main(String[] arg) {
        final List<File> files = FileUtils.getNonLocalDiskPath();
        final File toFolder = new File("build/copy");
        if (!toFolder.exists()) {
            toFolder.mkdirs();
        }

        while (true) {
            for (File file : files) {
                System.out.println("Find non local disk path: " + file.getAbsolutePath() + "/" + file.getName());
                copy(file, toFolder);
            }
        }
    }

    private static void copy(final File file, final File toFolder) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                copy(child, toFolder);
            }
            return;
        }
        copyInternal(file, toFolder);
    }

    private static void copyInternal(final File file, final File toFolder) {
        for (String ext : EXTENSITIONS) {
            if (file.getName().endsWith(ext)) {
                try {
                    if (set.contains(file.getName())) {
                        return;
                    }
                    final FileInputStream fromFile = new FileInputStream(file);
                    final FileOutputStream toFile = new FileOutputStream(new File(toFolder, file.getName()));
                    final byte[] buffer = new byte[BUFFER_SIZE];
                    int byteRead = 0;
                    do {
                        byteRead = fromFile.read(buffer);
                        if (byteRead > 0) {
                            toFile.write(buffer, 0, byteRead);
                        }
                    } while (byteRead >= 0);

                    System.out.println("copied file from " +
                            file.getAbsolutePath() +
                            "/" +
                            file.getName() +
                            " to " +
                            toFolder.getAbsolutePath() +
                            "/" +
                            toFolder.getName());
                    fromFile.close();
                    toFile.close();
                    set.add(file.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
