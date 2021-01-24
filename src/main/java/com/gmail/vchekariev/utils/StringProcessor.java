package com.gmail.vchekariev.utils;

import java.util.Set;
import java.util.regex.Pattern;


public interface StringProcessor {

    static Set<String> getStopwords() {
        String stopwords = "a,the,and,of,to,is,in,/><br";
        return Set.of(stopwords.split(","));
    }

    static String removeTags(String input) {
        Pattern noTags = Pattern.compile("\\<.*?\\>");
        return noTags.matcher(input).replaceAll("");
    }

    static String removeNumbers(String input) {
        Pattern noNumber = Pattern.compile("[^a-z|A-Z]");
        return noNumber.matcher(input).replaceAll("");
    }

}
