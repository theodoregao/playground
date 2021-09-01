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
import java.util.List;

import static com.playground.constant.DefaultArguments.DEFAULT_METADATA;
import static com.playground.constant.DefaultArguments.DEFAULT_OUTPUT_FOLDER;

public class RecordManager {

    private final Gson gson;
    private final File jsonFile;

    public RecordManager(ArgumentManager argumentManager) {
        final String outputFolderArgument = argumentManager.getArgumentValue(ArgumentConfigs.OUT).get(0);
        final File outputFolder = new File(outputFolderArgument == null ? DEFAULT_OUTPUT_FOLDER : outputFolderArgument);
        final String metadataArgument = argumentManager.getArgumentValue(ArgumentConfigs.METADATA).get(0);
        this.jsonFile = new File(outputFolder, metadataArgument == null ? DEFAULT_METADATA : metadataArgument);

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd hh:mm:ss a")
                .create();
    }

    public List<FileMetadata> loadFileMetadata() {
        List<FileMetadata> fileMetadatas = null;
        try {
            if (!jsonFile.exists()) {
                fileMetadatas = new ArrayList<>();
            } else {
                final Type fileMetadataType = new TypeToken<ArrayList<FileMetadata>>(){}.getType();
                fileMetadatas = gson.fromJson(new BufferedReader(new FileReader(jsonFile)), fileMetadataType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileMetadatas;
    }

    public void saveFileMetadata(List<FileMetadata> fileMetadatas) {
        try {
            final FileWriter fileWriter = new FileWriter(jsonFile);
            gson.toJson(fileMetadatas, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
