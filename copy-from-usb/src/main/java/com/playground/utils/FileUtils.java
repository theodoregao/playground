package com.playground.utils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public static String checksum(File file) {
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            if (!file.exists()) {
                return null;
            }
            final FileInputStream fis = new FileInputStream(file);
            md5.update(fis.readAllBytes());
            fis.close();
            final byte[] md5Hashcode = md5.digest();
            return bytesToHexString(md5Hashcode);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
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

    private static String bytesToHexString(byte[] bytes) {
        final StringBuilder sb = new StringBuilder();
        for (byte b: bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
