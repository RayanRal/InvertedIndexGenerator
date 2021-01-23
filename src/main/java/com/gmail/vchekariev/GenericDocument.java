package com.gmail.vchekariev;

public class GenericDocument implements Comparable<GenericDocument> {

    private String fileName;
    private String text;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public GenericDocument(String fileName, String text) {
        this.fileName = fileName;
        this.text = text;
    }

    public String toString() {
        return getFileName();
    }


    @Override
    public int compareTo(GenericDocument o) {
        return CharSequence.compare(this.getFileName(), o.getFileName());
    }

}