package com.gmail.vchekariev;

import com.gmail.vchekariev.index.InvertedIndex;
import com.gmail.vchekariev.index.InvertedIndexWrapper;
import com.gmail.vchekariev.utils.Benchmark;
import com.gmail.vchekariev.utils.FileUtils;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;


public class IndexGenerationRunner {

    private static final int NUMBER_OF_INDEXER_THREADS = 8; //Property.getInt("numOfThreads");
    private static final String DEFAULT_INPUT_DIR = FileUtils.INPUT_PATH;
    private static final String DEFAULT_EXTENSION = ".txt";

    public static void main(String[] args) {
        Benchmark benchmark = new Benchmark();
        benchmark.startTimer("total");

        Queue<String> filesToIndex = getFilesToIndex(args);
        if (filesToIndex == null) return;

        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_INDEXER_THREADS);

        InvertedIndex index = new InvertedIndexWrapper();
        for (int x = 0; x < NUMBER_OF_INDEXER_THREADS; x++) {
            executorService.execute(new Indexer(filesToIndex, index));
        }

//         1 thread total - 152615
//         2 thread total - 42728
//         4 thread total - 25269
//         8 thread total - 4063
//        (something very unlinear, needs more measuring)

        // Wait till all files will be processed
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(index.size());
        System.out.println(index.getTopWords(10));
//        System.out.println(index.keySet());

        //Time to write the index to a file
        benchmark.startTimer("writing-to-file");
//		SPIMIReconciliation.reconciliate();
        benchmark.stopTimer("writing-to-file");
        benchmark.stopTimer("total");


        // Display some statistics
        System.out.println("Finished creating inverted index.");

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
