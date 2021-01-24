package com.gmail.vchekariev.index;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class InvertedIndex {

    private final Map<String, List<String>> tokenIndex;

    public InvertedIndex() {
        this.tokenIndex = new ConcurrentHashMap<>();
    }

    public InvertedIndex(Map<String, List<String>> tokenIndex) {
        this.tokenIndex = tokenIndex;
    }

    public boolean add(String token, String filename) {
        if (token == null) {
            return false;
        }
        synchronized (tokenIndex) {
            tokenIndex.putIfAbsent(token, Collections.synchronizedList(new ArrayList<>()));
            tokenIndex.get(token).add(filename);
        }
        return true;
    }

    public int size() {
        return tokenIndex.size();
    }

    public List<String> getTopWords(int count) {
        return tokenIndex.entrySet().stream()
                .sorted(Comparator.comparingInt(
                        (Map.Entry<String, List<String>> value) -> value.getValue().size()).reversed())
                .map(Map.Entry::getKey)
                .limit(count)
                .collect(Collectors.toList());
    }

    public Set<String> getFilesForToken(String token) {
        return new HashSet<>(tokenIndex.getOrDefault(token, new ArrayList<>()));
    }

    /**
     * Validate the index to make sure it holds the following properties for every term
     * - The list of PostingList is not null
     * - The list of PostingList contains only unique document id
     *
     * @return result of the validation
     */
    public boolean validate() {
        Set<String> docSet = new HashSet<>();
        for (String term : tokenIndex.keySet()) {
            List<String> files = tokenIndex.get(term);
            if (files == null) return false;
            docSet.clear();
            for (String file : files) {
                if (docSet.contains(file))
                    return false;
                else
                    docSet.add(file);
            }
        }
        return true;
    }

    public void writeToFile(String outputPath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputPath))) {
            oos.writeObject(tokenIndex);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

