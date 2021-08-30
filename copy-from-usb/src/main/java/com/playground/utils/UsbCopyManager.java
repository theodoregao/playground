package com.playground.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class UsbCopyManager {

    private static final String DEFAULT_OUTPUT_FOLDER = "build/copy";
    private static final List<String> DEFAULT_EXTENSIONS = List.of("txt");
    private static final int BUFFER_SIZE = 1024;

    private final File outputFolder;
    private final List<String> extensions;

    public UsbCopyManager(List<String> extensions, String outputFolder) {
        this.outputFolder = new File(outputFolder == null ? DEFAULT_OUTPUT_FOLDER : outputFolder);
        if (!this.outputFolder.exists()) {
            this.outputFolder.mkdirs();
        }

        this.extensions = extensions == null ? DEFAULT_EXTENSIONS : extensions;
    }

    public void copy(final File file) {
        if (file.isFile()) {
            copyInternal(file, outputFolder);
            return;
        }

        final File[] files = file.listFiles();
        if (files == null) {
            return;
        }

        for (File child : files) {
            copy(child);
        }
    }

    private void copyInternal(final File file, final File toFolder) {
        for (String ext : extensions) {
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
