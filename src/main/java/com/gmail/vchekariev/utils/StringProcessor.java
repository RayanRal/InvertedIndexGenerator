package com.gmail.vchekariev.utils;

import java.util.Set;
import java.util.regex.Pattern;


public interface StringProcessor {

    static Set<String> getStopwords() {
        String stopwords = "a,the,and,of,to,is,in,/><br";
        return Set.of(stopwords.split(","));
    }

    Pattern noTags = Pattern.compile("\\<.*?\\>");

    static String removeTags(String input) {
        return noTags.matcher(input).replaceAll("");
    }

    Pattern noEntities = Pattern.compile("\\&.*?\\;");

    static String removeEntities(String input) {
        return noEntities.matcher(input).replaceAll("");
    }

    Pattern noNumber = Pattern.compile("[^a-z|A-Z]");

    static String noNumber(String input) {
        return noNumber.matcher(input).replaceAll("");
    }

}
