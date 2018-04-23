package com.example.janek.jcommanderandroid;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Janek on 17.04.2018.
 */

public class FileModel {

    private File currentDirectory;
    private String fileName;
    private String fileSize;
    private String fileLastModified;
    private boolean selected;

    public FileModel(File currentDirectory) {
        this.currentDirectory = currentDirectory;
        this.fileName = currentDirectory.getName();
        this.fileSize = String.valueOf(Math.round(currentDirectory.length()/1024.0) + "k");
        this.fileLastModified = DateFormat.getDateInstance().format(currentDirectory.lastModified());
        this.selected = false;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileLastModified() {
        return fileLastModified;
    }

    public void setFileLastModified(String fileLastModified) {
        this.fileLastModified = fileLastModified;
    }

    public File getCurrentDirectory() {
        return currentDirectory;
    }

    public void setCurrentDirectory(File currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ArrayList<FileModel> getFileListModel() {
        ArrayList<FileModel> files = new ArrayList<>();
        if(!getCurrentDirectory().getAbsolutePath().equals("/storage")) {
            FileModel parentFile = new FileModel(getCurrentDirectory().getParentFile());
            parentFile.setFileName("..");
            parentFile.setFileLastModified("");
            parentFile.setFileSize("");
            files.add(parentFile);
        }
        for(File file : getCurrentDirectory().listFiles()){
            files.add(new FileModel(file));
        }
        return files;

    }
}
