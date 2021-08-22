package com.playground.utils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * FileUtils provides static utils methods related to system files.
 */
public class FileUtils {

    private static final String LOCAL_DISK = "Local Disk";

    public static List<File> getNonLocalDiskPath() {
        if (isMacOs()) { // for Mac OS
            return getMacOsNonLocalDiskPath();
        } else { // for Windows
            return getWindowsNonLocalDiskPath();
        }
    }

    private static boolean isMacOs() {
        return System.getProperty("os.name").startsWith("Mac OS");
    }

    private static List<File> getMacOsNonLocalDiskPath() {
        final List<File> nonLocalDisks = new ArrayList<>();
        final FileSystemView fsv = FileSystemView.getFileSystemView();
        for (File file: new File("/Volumes").listFiles()) {
            if (file.canRead() && file.canWrite() && file.getName().endsWith("USB")) {
                nonLocalDisks.add(file);
            }
        }
        return nonLocalDisks;
    }

    private static List<File> getWindowsNonLocalDiskPath() {
        final List<File> nonLocalDisks = new ArrayList<>();
        final FileSystemView fsv = FileSystemView.getFileSystemView();
        for (File file: File.listRoots()) {
            if (!fsv.getSystemDisplayName(file).contains(LOCAL_DISK)) {
                nonLocalDisks.add(file);
            }
        }
        return nonLocalDisks;
    }
}
