package com.gmail.vchekariev.index;

import com.gmail.vchekariev.utils.StringProcessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.StringTokenizer;


public class Indexer implements Runnable {

    private final InvertedIndex index;
    private final Queue<String> filesToIndex;

    public Indexer(Queue<String> filesToIndex, InvertedIndex index) {
        this.filesToIndex = filesToIndex;
        this.index = index;
    }

    public void run() {
        System.out.println("Indexer started");
        String fileName = filesToIndex.poll();
        while (fileName != null) {
            try {
                GenericDocument doc = readFile(fileName);
                processDocument(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileName = filesToIndex.poll();
        }
    }

    private void processDocument(GenericDocument document) {
        StringTokenizer st = new StringTokenizer(document.getText());
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            token = processToken(token);

            if (token != null) {
                index.add(token, document.getFileName());
            }
        }
    }

    private GenericDocument readFile(String filename) throws IOException {
        String text = Files.readString(Paths.get(filename));
        return new GenericDocument(filename, text);
    }

    public String processToken(String token) {
        token = token.toLowerCase();

        if (token.isEmpty())
            return null;

        if (StringProcessor.getStopwords().contains(token))
            return null;

        String noTags = StringProcessor.removeTags(token);
        String cleanToken = StringProcessor.removeNumbers(noTags);

        return cleanToken;
    }

}
