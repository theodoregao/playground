package com.playground;

import com.playground.argument.ArgumentManager;
import com.playground.argument.ArgumentManagerImpl;
import com.playground.constants.ArgumentConfigs;
import com.playground.utils.DiskMonitor;
import com.playground.utils.UsbCopyManager;

import java.io.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        final ArgumentManager argumentManager = ArgumentManagerImpl.createArgumentManager(args);
        if (argumentManager.getArgumentValue(ArgumentConfigs.HELP) != null) {
            argumentManager.print();
            return;
        }

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
