package com.playground;

import com.playground.utils.FileUtils;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    private static final List<String> EXTENSIONS = List.of("txt", "png", "jpg");
    private static final int BUFFER_SIZE = 1024;
    private static final int SLEEP_TIME = 60 * 1000;
    private static final Set<String> set = new HashSet<>();

    public static void main(String[] arg) throws InterruptedException {
        final List<File> files = FileUtils.getNonLocalDiskPath();
        final File toFolder = new File("build/copy");
        if (!toFolder.exists()) {
            toFolder.mkdirs();
        }

        while (true) {
            for (File file : files) {
                System.out.println("Find USB disk path: " + file.getAbsolutePath());
                copy(file, toFolder);
            }
            Thread.sleep(SLEEP_TIME);
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
        for (String ext : EXTENSIONS) {
            if (file.getName().endsWith(ext)) {
                try {
                    if (set.contains(file.getAbsolutePath())) {
                        return;
                    }
                    final FileInputStream fromFile = new FileInputStream(file);
                    final FileOutputStream toFile = new FileOutputStream(
                            new File(toFolder, file.getAbsolutePath().replaceAll("/", "_")));
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
                    set.add(file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
