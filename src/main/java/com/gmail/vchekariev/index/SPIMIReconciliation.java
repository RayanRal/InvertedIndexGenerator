package com.gmail.vchekariev.index;

import com.gmail.vchekariev.utils.FileUtils;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class SPIMIReconciliation {

    public static void reconciliate() {
        int totalBlock = InvertedIndexWrapper.getTotalBlock();

        LineNumberReader[] buffReadArr = new LineNumberReader[totalBlock + 1];
        String[] lastLineRead = new String[totalBlock + 1];

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(FileUtils.OUTPUT_PATH + "/index.txt"));

            for (int i = 0; i <= totalBlock; i++) {
                buffReadArr[i] = new LineNumberReader(new FileReader(FileUtils.OUTPUT_PATH + "/" + i + ".spimi"));
                lastLineRead[i] = null;
            }

            String currentToken = findFirstToken(buffReadArr, lastLineRead);
            out.write(currentToken);
            while (currentToken != null) {
                Set<String> files = obtainFilesForToken(currentToken, buffReadArr, lastLineRead);
                for (String i : files) {
                    out.write(" " + i);
                }
                currentToken = findFirstToken(buffReadArr, lastLineRead);
                out.write("\n" + currentToken);
            }

            out.close();

            //Get rid of these files now.
            for (int i = 0; i <= totalBlock; i++) {
                (new File(FileUtils.OUTPUT_PATH + "/" + i + ".spimi")).delete();
            }

            //Signal the corpus we are done reconciling.
//            CorpusFactory.getCorpus().closeIndex();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Set<String> obtainFilesForToken(
            String currentToken,
            LineNumberReader[] buffReadArr,
            String[] lastLineRead) {
        Set<String> result = new HashSet<>();
        for (int i = 0; i < buffReadArr.length; i++) {
            String line = lastLineRead[i];
            if (line == null)
                continue;
            String token = readUntil(line, ' ');
            if (!token.equals(currentToken)) {
                continue;
            } else {
                lastLineRead[i] = null; //reset that line to allow the reading of another one
                boolean skip = true;
                for (String s : line.split(" ")) {
                    if (skip) {
                        skip = false;
                        continue;
                    }
//                    String p = Posting.fromString(token, s);
//                    if (!result.add(p))
//                        for (Posting p2 : result) {
//                            if (p.equals(p2))
//                                p2.add(p.getOccurence());
//                        }
                }
            }
        }
        return result;
    }

    private static String findFirstToken(LineNumberReader[] buffReadArr, String[] lastLineRead) throws IOException {
        String smallestToken = null;
        for (int i = 0; i < buffReadArr.length; i++) {
            if (lastLineRead[i] == null)
                lastLineRead[i] = buffReadArr[i].readLine();
            String line = lastLineRead[i];
            if (line == null)
                continue;
            String token = readUntil(line, ' ');
            if (smallestToken == null)
                smallestToken = token;
            else if (token.compareTo(smallestToken) < 0)
                smallestToken = token;
        }
        return smallestToken;
    }

    private static String readUntil(String line, char cha) {
        return line.substring(0, line.indexOf(cha));
    }
}
