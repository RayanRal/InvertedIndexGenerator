package com.gmail.vchekariev.index;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface InvertedIndex {

	boolean add(String token, String filename);

	Set<String> keySet();

	int size();

	Set<String> getFilesForToken(String token);

	Iterator<String> iterator();

	void clear();
	
	Set<String> getAll();

	List<String> getTopWords(int count);

}