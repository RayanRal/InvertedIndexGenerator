package com.gmail.vchekariev;

import com.gmail.vchekariev.index.Indexer;
import com.gmail.vchekariev.index.InvertedIndex;
import com.gmail.vchekariev.utils.Benchmark;
import com.gmail.vchekariev.utils.FileUtils;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;


public class IndexGenerationRunner {

    private static final int NUMBER_OF_INDEXER_THREADS = 4; //Property.getInt("numOfThreads");
    private static final String DEFAULT_INPUT_DIR = FileUtils.INPUT_PATH;
    private static final String DEFAULT_EXTENSION = ".txt";

    public static void main(String[] args) {
        Benchmark benchmark = new Benchmark();
        benchmark.startTimer(Benchmark.TOTAL);

        benchmark.startTimer(Benchmark.READ_FILE_NAMES);
        Queue<String> filesToIndex = getFilesToIndex(args);
        if (filesToIndex == null) return;
        benchmark.stopTimer(Benchmark.READ_FILE_NAMES);

        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_INDEXER_THREADS);

        benchmark.startTimer(Benchmark.INDEX_FILES);
        InvertedIndex index = new InvertedIndex();
        for (int x = 0; x < NUMBER_OF_INDEXER_THREADS; x++) {
            executorService.execute(new Indexer(filesToIndex, index));
        }

        // Wait till all files will be processed
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        benchmark.stopTimer(Benchmark.INDEX_FILES);
        System.out.println(index.size());
        System.out.println(index.getTopWords(10));

        //Time to write the index to a file
        benchmark.startTimer(Benchmark.WRITE_TO_FILE);
        index.writeToFile(FileUtils.OUTPUT_PATH + FileUtils.OUTPUT_FILE);
        benchmark.stopTimer(Benchmark.WRITE_TO_FILE);
        benchmark.stopTimer(Benchmark.TOTAL);

        System.out.println("Finished creating inverted index.");

        // Display some statistics
        benchmark.reportAll();
    }

    private static Queue<String> getFilesToIndex(String[] args) {
        String inputDirectory = (args.length > 0) ? args[0] : DEFAULT_INPUT_DIR;
        String extension = (args.length > 1) ? args[1] : DEFAULT_EXTENSION;

        //Read filenames
        List<String> filesToIndex = FileUtils.getAllFiles(inputDirectory, extension, false);
        if (filesToIndex == null) {
            return null;
        }
        return new ConcurrentLinkedQueue<>(filesToIndex);
    }

}
