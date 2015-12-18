package com.znb.java.model;

import java.io.File;

/**
 * @author zhangnaibin
 * @Date 2015-12-18 下午2:32
 */
public class MailAttach {
    private File file;
    private String fileName;
    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public MailAttach() {

    }
    public MailAttach(File file, String fileName) {
        super();
        this.file = file;
        this.fileName = fileName;
    }
}
