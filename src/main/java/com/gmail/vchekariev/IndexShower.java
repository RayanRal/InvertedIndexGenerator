package com.gmail.vchekariev;

import com.gmail.vchekariev.index.InvertedIndex;
import com.gmail.vchekariev.utils.FileUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

public class IndexShower {

    public static void main(String[] args) throws ClassNotFoundException {
        Map<String, List<String>> tokenIndex = readIndex(FileUtils.OUTPUT_PATH + FileUtils.OUTPUT_FILE);
        InvertedIndex index = new InvertedIndex(tokenIndex);
        System.out.println(index.getTopWords(20));
        System.out.println(index.getFilesForToken("movie"));
    }

    private static Map<String, List<String>> readIndex(String inputPath) throws ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputPath))) {
            Map<String, List<String>> tokenIndex = (Map<String, List<String>>) ois.readObject();
            return tokenIndex;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
