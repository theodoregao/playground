package com.playground.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.playground.argument.ArgumentManager;
import com.playground.bean.FileMetadata;
import com.playground.constant.ArgumentConfigs;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsbCopyManager {

    private static final String DEFAULT_OUTPUT_FOLDER = "build/copy";
    private static final List<String> DEFAULT_EXTENSIONS = List.of("txt");
    private static final String DEFAULT_METADATA = "metadata.json";
    private static final int BUFFER_SIZE = 1024;

    private final File outputFolder;
    private final List<String> extensions;
    private final File metadata;


    private final Gson gson;
    private List<FileMetadata> fileMetadatas;

    public UsbCopyManager(ArgumentManager argumentManager) {
        final String outputFolderArgument = argumentManager.getArgumentValue(ArgumentConfigs.OUT).get(0);
        outputFolder = new File(outputFolderArgument == null ? DEFAULT_OUTPUT_FOLDER : outputFolderArgument);
        if (!this.outputFolder.exists()) {
            this.outputFolder.mkdirs();
        }
        final String extensionsArgument = argumentManager.getArgumentValue(ArgumentConfigs.EXTENSIONS).get(0);
        extensions = extensionsArgument == null ? DEFAULT_EXTENSIONS : List.of(extensionsArgument.split(","));
        final String metadataArgument = argumentManager.getArgumentValue(ArgumentConfigs.METADATA).get(0);
        metadata = new File(outputFolder, metadataArgument == null ? DEFAULT_METADATA : metadataArgument);

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd hh:mm:ss a")
                .create();
    }

    public void copyUsb(final File usbFolder) {
        try {
            if (fileMetadatas == null) {
                fileMetadatas = new ArrayList<>();
            } else {
                final Type fileMetadataType = new TypeToken<ArrayList<FileMetadata>>(){}.getType();
                fileMetadatas = gson.fromJson(new BufferedReader(new FileReader(metadata)), fileMetadataType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        copy(usbFolder);

        try {
            final FileWriter fileWriter = new FileWriter(metadata);
            gson.toJson(fileMetadatas, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
