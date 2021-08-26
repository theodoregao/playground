package com.playground.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * DiskMonitor class helps to monitor the disk updates, and notify the Observer about the changes.
 */
public class DiskMonitor {

    public interface DiskMonitorListener {
        void onDiskConnected(List<File> paths);
        void onDiskDisconnected(List<File> paths);
    }

    private static final long CHECK_INTERVAL = 1000;

    private Thread workThread;
    private List<File> disks;
    private DiskMonitorListener diskMonitorListener;

    public DiskMonitor() {
        workThread = new WorkerThread();
        disks = List.of();
    }

    public void setDiskMonitorListener(DiskMonitorListener diskMonitorListener) {
        this.diskMonitorListener = diskMonitorListener;
    }

    public void start() {
        workThread.start();
        try {
            workThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class WorkerThread extends Thread {
        @Override
        public void run() {
            while (true) {
                final List<File> newDisks = FileUtils.getNonLocalDiskPath();
                if (diskMonitorListener != null) {
                    final List<File> newAddedDisks = new ArrayList<>();
                    for (File file: newDisks) {
                        if (!disks.contains(file)) {
                            newAddedDisks.add(file);
                        }
                    }
                    if (!newDisks.isEmpty()) {
                        diskMonitorListener.onDiskConnected(newAddedDisks);
                    }

                    final List<File> removedDisks = new ArrayList<>();
                    for (File file: disks) {
                        if (!newDisks.contains(file)) {
                            removedDisks.add(file);
                        }
                    }
                    if (!removedDisks.isEmpty()) {
                        diskMonitorListener.onDiskDisconnected(removedDisks);
                    }
                }
                disks = newDisks;

                try {
                    sleep(CHECK_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
