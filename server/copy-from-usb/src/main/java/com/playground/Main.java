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
                List.of(EXTENSIONS, HELP, OUT, METADATA, FILE),
                args
        );

        if (argumentManager.getArgumentValue(HELP) != null) {
            System.out.println(argumentManager.getArgumentDescription());
            return;
        }

        final UsbCopyManager usbCopyManager = new UsbCopyManager(argumentManager);
        if (argumentManager.getArgumentValue(FILE) != null) {
            usbCopyManager.filePrint();
            return;
        }

        final DiskMonitor diskMonitor = new DiskMonitor();
        diskMonitor.setDiskMonitorListener(new DiskMonitor.DiskMonitorListener() {
            @Override
            public void onDiskConnected(List<File> paths) {
                for (File path : paths) {
                    System.out.println("USB disk connected path: " + path.getAbsolutePath());
                    usbCopyManager.copyUsb(path);
                    usbCopyManager.filePrint();
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
