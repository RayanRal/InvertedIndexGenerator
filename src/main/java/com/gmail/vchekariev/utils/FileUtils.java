package com.gmail.vchekariev.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;


public class FileUtils {

    public static final String INPUT_PATH = "src/main/resources/datasets/aclImdb/data1";
    public static final String OUTPUT_PATH = "src/main/resources/output/";
    public static final String OUTPUT_FILE = "tokenIndex.serialized";

    /**
     * Look for files matching an extension in a given folder
     *
     * @param directoryPath path to look for
     * @param extension     extension to filter for
     * @param relativePath  if true will return the path of the file relative to the directoryPath. False will return a full path
     * @return array of string, one string per found file
     */
    public static List<String> getAllFiles(String directoryPath, final String extension, boolean relativePath) {
        File dir = new File(directoryPath);

        FilenameFilter filter = (dir1, name) -> {
            if (name.startsWith("."))
                return false;
            return name.endsWith(extension);
        };

        String[] r = dir.list(filter);
        if (r != null) {
            if (!relativePath) {
                for (int i = 0; i < r.length; i++) {
                    r[i] = dir.getAbsolutePath() + "/" + r[i];
                }
            }
            return Arrays.asList(r);
        } else {
            return null;
        }
    }



}
