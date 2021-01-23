package com.gmail.vchekariev.utils;

import java.util.Set;


public interface StopWordRemover {

	static Set<String> getStopwords(){
        String stopwords = "a,the,and,of,to,is,in,/><br";
        return Set.of(stopwords.split(","));
	}
	
}
