package com.playground.utils;

import com.playground.argument.ArgumentManager;
import com.playground.bean.FileMetadata;
import com.playground.constant.ArgumentConfigs;

import java.io.*;
import java.util.Date;
import java.util.List;

import static com.playground.constant.DefaultArguments.DEFAULT_EXTENSIONS;
import static com.playground.constant.DefaultArguments.DEFAULT_OUTPUT_FOLDER;

public class UsbCopyManager {
    private static final int BUFFER_SIZE = 1024;

    private final File outputFolder;
    private final List<String> extensions;
    private final RecordManager recordManager;

    private List<FileMetadata> fileMetadatas;

    public UsbCopyManager(ArgumentManager argumentManager) {
        final String outputFolderArgument = argumentManager.getArgumentValue(ArgumentConfigs.OUT).get(0);
        outputFolder = new File(outputFolderArgument == null ? DEFAULT_OUTPUT_FOLDER : outputFolderArgument);
        if (!this.outputFolder.exists()) {
            this.outputFolder.mkdirs();
        }

        final String extensionsArgument = argumentManager.getArgumentValue(ArgumentConfigs.EXTENSIONS).get(0);
        extensions = extensionsArgument == null ? DEFAULT_EXTENSIONS : List.of(extensionsArgument.split(","));

        recordManager = new RecordManager(argumentManager);
    }

    public void copyUsb(final File usbFolder) {
        fileMetadatas = recordManager.loadFileMetadata();
        copy(usbFolder);
        recordManager.saveFileMetadata(fileMetadatas);
    }

    private void copy(final File file) {
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

                    final FileMetadata metadata = new FileMetadata();
                    metadata.setFileName(dstFilename);
                    metadata.setOriginalFileName(filename);
                    metadata.setSrc(file.getAbsolutePath());
                    metadata.setDate(new Date(System.currentTimeMillis()));
                    fileMetadatas.add(metadata);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
