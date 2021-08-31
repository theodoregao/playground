package com.playground;

import com.playground.argument.ArgumentManager;
import com.playground.argument.ArgumentManagerImpl;
import com.playground.utils.DiskMonitor;
import com.playground.utils.UsbCopyManager;

import java.io.*;
import java.util.List;

import static com.playground.constant.ArgumentConfigs.*;

public class Main {

    public static void main(String[] args) {
        final ArgumentManager argumentManager = ArgumentManagerImpl.createArgumentManager(
                List.of(EXTENSIONS, HELP, OUT),
                args
        );

        final UsbCopyManager usbCopyManager = new UsbCopyManager(argumentManager);

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
