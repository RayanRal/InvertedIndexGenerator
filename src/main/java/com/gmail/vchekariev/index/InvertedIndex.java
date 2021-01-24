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

    public void add(String token, String filename) {
        if (token == null) {
            return;
        }
        tokenIndex.putIfAbsent(token, Collections.synchronizedList(new ArrayList<>()));
        tokenIndex.computeIfPresent(token, (key, value) -> {
            value.add(filename);
            return value;
        });
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

    public void writeToFile(String outputPath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputPath))) {
            oos.writeObject(tokenIndex);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

