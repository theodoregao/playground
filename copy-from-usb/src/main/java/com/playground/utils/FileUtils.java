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
