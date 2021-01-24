package com.gmail.vchekariev;

public class GenericDocument implements Comparable<GenericDocument> {

    private String filePath;
    private String fileName;
    private String text;

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
        this.fileName = filePath.substring(filePath.lastIndexOf("\\")+1);
    }

    public String toString() {
        return getFilePath();
    }


    @Override
    public int compareTo(GenericDocument o) {
        return CharSequence.compare(this.getFilePath(), o.getFilePath());
    }

}