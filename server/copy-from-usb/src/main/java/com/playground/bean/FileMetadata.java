package com.playground.bean;

import java.util.Date;

public class FileMetadata {

    private String fileName;
    private String originalFileName;
    private Date date;
    private String src;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public String toString() {
        return "FileMetadata{" +
                "fileName='" + fileName + '\'' +
                ", originalFileName='" + originalFileName + '\'' +
                ", date=" + date +
                ", src='" + src + '\'' +
                '}';
    }
}
