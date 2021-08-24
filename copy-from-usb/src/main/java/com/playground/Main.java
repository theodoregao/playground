package com.playground;

import com.playground.utils.DiskMonitor;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    private static final List<String> EXTENSIONS = List.of("txt", "png", "jpg");
    private static final int BUFFER_SIZE = 1024;
    private static final Set<String> set = new HashSet<>();

    public static void main(String[] arg) {
        final File toFolder = new File("build/copy");
        if (!toFolder.exists()) {
            toFolder.mkdirs();
        }

        final DiskMonitor diskMonitor = new DiskMonitor();
        diskMonitor.setDiskMonitorListener(new DiskMonitor.DiskMonitorListener() {
            @Override
            public void onDiskConnected(List<File> paths) {
                for (File path : paths) {
                    System.out.println("USB dis connected path: " + path.getAbsolutePath());
                    copy(path, toFolder);
                }
            }

            @Override
            public void onDiskDisconnected(List<File> paths) {
                for (File path : paths) {
                    System.out.println("USB disk removed: " + path.getAbsolutePath());
                    final Set<String> removeFiles = new HashSet<>();
                    for (String str: set) {
                        if (str.startsWith(path.getPath())) {
                            removeFiles.add(str);
                        }
                    }
                    set.removeAll(removeFiles);
                }
            }
        });
        diskMonitor.start();
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
