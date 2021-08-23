package com.playground;

import com.playground.utils.FileUtils;

import java.io.*;
import java.util.List;

public class Main {

    private static final List<String> EXTENSITIONS = List.of("txt", "png", "jpg");
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] arg) {
        final List<File> files = FileUtils.getNonLocalDiskPath();
        final File toFolder = new File("build/copy");
        if (!toFolder.exists()) {
            toFolder.mkdirs();
        }

        for (File file : files) {
            System.out.println("Find USB disk path: " + file.getAbsolutePath());
            copy(file, toFolder);
        }
    }

    private static void copy(final File file, final File toFolder) {
        if (file.isFile()) {
            copyInternal(file, toFolder);
            return;
        }

        final File[] files = file.listFiles();

        if (files == null) {
            return;
        }

        for (File child : files) {
            copy(child, toFolder);
        }
    }

    private static void copyInternal(final File file, final File toFolder) {
        for (String ext : EXTENSITIONS) {
            if (file.getName().endsWith(ext)) {
                try {
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
                            " to " +
                            toFolder.getAbsolutePath());
                    fromFile.close();
                    toFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
