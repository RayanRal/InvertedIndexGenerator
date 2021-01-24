package com.gmail.vchekariev.index;

public class GenericDocument {

    private String filePath;
    private final String fileName;
    private String text;
    private static final String DELIMITER = "/";

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFileName() {
        return fileName;
    }

    public GenericDocument(String filePath, String text) {
        this.filePath = filePath;
        this.text = text;
        this.fileName = filePath.substring(filePath.lastIndexOf(DELIMITER)+1);
    }

}
