package com.gmail.vchekariev.utils;

import java.util.Map;
import java.util.TreeMap;


public class Benchmark {

    public static final String TOTAL = "total";
    public static final String READ_FILE_NAMES = "readFileNames";
    public static final String INDEX_FILES = "indexFiles";
    public static final String WRITE_TO_FILE = "writeToFile";

    private final Map<String, BenchmarkCounter> counters = new TreeMap<>();

    public void startTimer(String name) {
        create(name);
        counters.get(name).start();
    }

    public void stopTimer(String name) {
        create(name);
        counters.get(name).stop();
    }

    public void reportCounter(String name) {
        BenchmarkCounter counter = counters.get(name);
        System.out.println(counter.getName() + " is running for " + counter.getDuration() + " ms");
    }

    private void create(String name) {
        if (!counters.containsKey(name)) {
            counters.put(name, new BenchmarkCounter(name));
        }
    }

    public void reportAll() {
        counters.forEach((counterName, time) -> {
            System.out.println(counterName + " is running for " + time.getDuration());
        });
    }
}

class BenchmarkCounter {

    private String name;
    private long start = 0;
    private long runningFor = 0;

    public BenchmarkCounter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    public void stop() {
        runningFor += System.currentTimeMillis() - start;
        start = 0;
    }

    public long getDuration() {
        return runningFor;
    }

}
