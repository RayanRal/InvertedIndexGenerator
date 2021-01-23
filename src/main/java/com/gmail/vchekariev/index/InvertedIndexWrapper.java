package com.gmail.vchekariev.index;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class InvertedIndexWrapper implements InvertedIndex {

//	private static final int MEMORY_SIZE = Property.getInt("SPIMI_MEMORY_SIZE");
	private static Integer TotalBlockCounter = 0;
	private int currentBlockNumber;
	private int currentSize = 0;
	private DefaultInvertedIndex postingList = new DefaultInvertedIndex();

	public void flushBlock() {
		if (currentSize > 0) {
			System.out.println("Flushing block " + currentBlockNumber);
			postingList.writeToFile(String.valueOf(currentBlockNumber) + ".spimi");
			postingList = new DefaultInvertedIndex();
			currentSize = 0;
		}
		
	}

	private void acquireNewBlock() {
		synchronized(TotalBlockCounter) {
			currentBlockNumber = TotalBlockCounter++;
		}
	}
	
	@Override
	public boolean add(String token, String filename) {
//		if (currentSize >= MEMORY_SIZE) {
//			flushBlock();
//		}
		if (currentSize == 0)
			acquireNewBlock();
		
		//If the defaultPostingList tells me this is a new term/doc I increment size
		if (postingList.add(token, filename))
			currentSize++;
		
		return true;
	}

	@Override
	public Set<String> keySet() {
		return postingList.keySet();
	}

	@Override
	public int size() {
		return postingList.size();
	}

	@Override
	public Set<String> getFilesForToken(String token) {
		return postingList.getFilesForToken(token);
	}

	@Override
	public Iterator<String> iterator() {
		return postingList.iterator();
	}

	@Override
	public void clear() {
	}
	
	public static int getTotalBlock() {
		return TotalBlockCounter-1;
	}
	
	@Override
	public Set<String> getAll() {
		return postingList.getAll();
	}

	@Override
	public List<String> getTopWords(int count) {
		return postingList.getTopWords(count);
	}

}
