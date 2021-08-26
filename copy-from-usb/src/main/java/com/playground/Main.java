package com.playground;

import com.playground.utils.DiskMonitor;
import com.playground.utils.FileUtils;

import java.io.*;
import java.util.List;

public class Main {

    private static final List<String> EXTENSIONS = List.of("txt", "png", "jpg");
    private static final int BUFFER_SIZE = 1024;

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
                    System.out.println("USB disk connected path: " + path.getAbsolutePath());
                    copy(path, toFolder);
                }
            }

            @Override
            public void onDiskDisconnected(List<File> paths) {
                for (File path : paths) {
                    System.out.println("USB disk removed: " + path.getAbsolutePath());
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
                    final String checksum = FileUtils.checksum(file);
                    final String filename = file.getName();
                    final String dstFilename = checksum + filename.substring(filename.lastIndexOf('.'));
                    if (new File(toFolder, dstFilename).exists()) {
                        // the file already exist, return.
                        return;
                    }
                    final FileInputStream fromFile = new FileInputStream(file);
                    final FileOutputStream toFile = new FileOutputStream(new File(toFolder, dstFilename));
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
                            dstFilename);
                    fromFile.close();
                    toFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
