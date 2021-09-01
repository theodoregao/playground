package com.playground;

import com.playground.argument.ArgumentManager;
import com.playground.argument.ArgumentManagerImpl;
import com.playground.bean.FileMetadata;
import com.playground.utils.DiskMonitor;
import com.playground.utils.RecordManager;
import com.playground.utils.UsbCopyManager;

import java.io.*;
import java.util.List;

import static com.playground.constant.ArgumentConfigs.*;

public class Main {

    public static void main(String[] args) {
        final ArgumentManager argumentManager = ArgumentManagerImpl.createArgumentManager(
                List.of(EXTENSIONS, HELP, OUT, METADATA, FILE),
                args
        );

        // Handle help option
        if (argumentManager.getArgumentValue(HELP) != null) {
            System.out.println(argumentManager.getArgumentDescription());
            return;
        }

        // Handle search file option
        final UsbCopyManager usbCopyManager = new UsbCopyManager(argumentManager);
        final List<String> fileArgument = argumentManager.getArgumentValue(FILE);
        if (fileArgument != null) {
            searchFiles(argumentManager, List.of(fileArgument.get(0).split(",")));
            return;
        }

        // Handle detect USB and copy file option
        final DiskMonitor diskMonitor = new DiskMonitor();
        diskMonitor.setDiskMonitorListener(new DiskMonitor.DiskMonitorListener() {
            @Override
            public void onDiskConnected(List<File> paths) {
                for (File path : paths) {
                    System.out.println("USB disk connected path: " + path.getAbsolutePath());
                    usbCopyManager.copyUsb(path);
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

    private static void searchFiles(ArgumentManager argumentManager, List<String> files) {
        final RecordManager recordManager = new RecordManager(argumentManager);
        final List<FileMetadata> fileMetadatas = recordManager.loadFileMetadata();
        if (fileMetadatas == null || files == null) {
            return;
        }

        for (String file: files) {
            boolean isFound = false;
            for (int i = 0; i < fileMetadatas.size() && !isFound; i++) {
                if (fileMetadatas.get(i).getFileName().equals(file)) {
                    System.out.println(fileMetadatas.get(i));
                    isFound = true;
                }
            }
            if (!isFound) {
                System.out.println("Not found any record for " + file);
            }
        }
    }
}

