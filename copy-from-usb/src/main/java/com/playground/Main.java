package com.playground;

import com.playground.utils.DiskMonitor;
import com.playground.utils.UsbCopyManager;

import java.io.*;
import java.util.List;

public class Main {

    public static void main(String[] arg) {
        final List<String> EXTENSIONS = List.of("txt", "png", "jpg");
        final String folder = "build/copy";

        final UsbCopyManager usbCopyManager = new UsbCopyManager(EXTENSIONS, folder);

        final DiskMonitor diskMonitor = new DiskMonitor();
        diskMonitor.setDiskMonitorListener(new DiskMonitor.DiskMonitorListener() {
            @Override
            public void onDiskConnected(List<File> paths) {
                for (File path : paths) {
                    System.out.println("USB disk connected path: " + path.getAbsolutePath());
                    usbCopyManager.copy(path);
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
}
