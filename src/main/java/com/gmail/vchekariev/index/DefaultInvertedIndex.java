package com.gmail.vchekariev.index;

import com.gmail.vchekariev.utils.FileUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class DefaultInvertedIndex implements InvertedIndex {

    private final Map<String, List<String>> tokenIndex = new ConcurrentHashMap<>();

    void add(String token, List<String> documentList) {
        if (token == null) {
            return;
        }
        synchronized (tokenIndex) {
            tokenIndex.putIfAbsent(token, Collections.synchronizedList(new ArrayList<>()));
            tokenIndex.get(token).addAll(documentList);
        }
    }

    @Override
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

    /*
     * This method merges index b into index a.
     */
//    public synchronized void mergeWith(DefaultInvertedIndex b) {
//        for (String tokenb : b) {
//            this.add(tokenb, b.tokenIndex.get(tokenb));
//        }
//    }

    @Override
    public Set<String> keySet() {
        return tokenIndex.keySet();
    }

    @Override
    public int size() {
        return tokenIndex.size();
    }

//	@Override
//	public AbstractSet<Posting> getSet(String token) {
//		List<Posting> c = map.get(token);
//		if (c == null)
//			return new HashSet<Posting>();
//
//		HashSet<Posting> r = new HashSet<Posting>(map.get(token).size());
//		for (Posting n : map.get(token))
//			r.add(n);
//				return r;
//	}


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

    @Override
    public Iterator<String> iterator() {
        return tokenIndex.keySet().iterator();
    }

    @Override
    public void clear() {
        tokenIndex.clear();
    }

    public synchronized void writeToFile(String path) {
        try {
            StringBuilder sb = new StringBuilder();
            FileWriter fstream = new FileWriter(FileUtils.OUTPUT_PATH + "/" + path);
            // For each token of the index
            for (String token : tokenIndex.keySet()) {
                // Write to the index file
                sb.append(token).append(" ");
                for (String i : this.getFilesForToken(token)) {
                    sb.append(i).append(" ");
                }
                sb.append("\n");
            }
            fstream.write(sb.toString());
            // Close the index file.
            fstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Set<String> getFilesForToken(String token) {
        return new HashSet<>(tokenIndex.getOrDefault(token, new ArrayList<>()));
    }

    public Set<String> getAll() {
        Set<String> all = new HashSet<>();
        for (String s : tokenIndex.keySet()) {
            all.addAll(tokenIndex.get(s));
        }
        return all;
    }

    @Override
    public List<String> getTopWords(int count) {
        return tokenIndex.entrySet().stream()
                .sorted(Comparator.comparingInt(
                        (Map.Entry<String, List<String>> value) -> value.getValue().size()).reversed())
                .map(Map.Entry::getKey)
                .limit(count)
                .collect(Collectors.toList());
    }
}

//	public static DefaultInvertedIndex readFromFile(String path) {
//		try {
//			TreeMap<String, ArrayList<Posting>> newMap = new TreeMap<String, ArrayList<Posting>>();
//
//			File inputFile = new File(Constants.basepath + "/" + path);
//			BenchmarkRow timer = new BenchmarkRow(null);
//			System.out.println("opening " + inputFile.getAbsolutePath());
//			FileReader fstream = new FileReader(inputFile);
//			BufferedReader in = new BufferedReader(fstream);
//			// For each token of the index
//			String line = in.readLine();
//
//
//			String term = null;
//			while (line != null) {
//				StringTokenizer st = new StringTokenizer(line);
//				boolean firstToken = true;
//				Posting[] postingList = new Posting[st.countTokens()-1];
//				int i=0;
//				while (st.hasMoreTokens()) {
//					if (firstToken==true) {
//						firstToken = false;
//						term = st.nextToken();
//					}
//					else {
//						postingList[i++] = Posting.fromString(term, st.nextToken());
//					}
//				}
//
//				newMap.put(term, new ArrayList<Posting>(Arrays.asList((postingList))));
//				line = in.readLine();
//			}
//
//			in.close();
//			timer.stop();
//			System.out.println("index took me " + timer.getDuration()/1000.0 + "ms to open");
//
//			DefaultInvertedIndex dii = new DefaultInvertedIndex();
//			dii.map = newMap;
//			return dii;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}

