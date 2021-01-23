package com.gmail.vchekariev;

import com.gmail.vchekariev.index.InvertedIndex;
import com.gmail.vchekariev.index.InvertedIndexWrapper;
import com.gmail.vchekariev.utils.StopWordRemover;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.StringTokenizer;


public class Indexer implements Runnable {

    private InvertedIndex index;
    private final Queue<String> filesToIndex;

    public Indexer(Queue<String> filesToIndex, InvertedIndex index) {
        this.filesToIndex = filesToIndex;
        this.index = index;
    }

    public void run() {
        System.out.println("Indexer started");
        String fileName = filesToIndex.poll();
        while (fileName != null) {
            GenericDocument doc = readFile(fileName);
//            System.out.println("Document received " + doc.getFileName());
            processDocument(doc);
//            CorpusFactory.getCorpus().addArticle(d);
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

    private GenericDocument readFile(String filename) {
        try {
            String text = Files.readString(Paths.get(filename));
            return new GenericDocument(filename, text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String processToken(String token) {
        token = token.toLowerCase();

        if (token.isEmpty())
            return null;

        if (StopWordRemover.getStopwords().contains(token))
            return null;

        return token;
    }

}
