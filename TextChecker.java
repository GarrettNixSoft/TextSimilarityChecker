import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TextChecker {

    public static void main(String[] args) {
        
        String usageStr = "Usage: <file 1> <file 2> [minimum match length]";

        // if the number of arguments provided is clearly wrong, inform the user on how to use this program
        if (args.length < 2 || args.length > 3) {
            System.out.println(usageStr);
            System.exit(-1);
        }

        // fetch the files
        String file1Path = args[0];
        String file2Path = args[1];

        File file1 = Paths.get(file1Path).toFile();
        File file2 = Paths.get(file2Path).toFile();

        // check the minimum match length (default is 3)
        int minimumMatchLength;
        if (args.length == 3) {
            try {
                minimumMatchLength = Integer.parseInt(args[2]);
            }
            catch (NumberFormatException e) {
                minimumMatchLength = 3; // default
                System.err.println("ERROR: Please enter a valid integer for the minimum match length.");
                System.exit(-1);
            }
        }
        else minimumMatchLength = 3; // default

        // confirm file existence
        if (!file1.exists()) {
            System.err.println("ERROR: File " + file1.getName() + " does not exist!");
            System.exit(-1);
        }
        else if (!file2.exists()) {
            System.err.println("ERROR: File " + file2.getName() + " does not exist!");
            System.exit(-1);
        }

        // read all words from file 1
        List<String> file1Words = new ArrayList<>();

        try (BufferedReader file1Stream = new BufferedReader(new FileReader(file1))) {
            
            String line;
            while ((line = file1Stream.readLine()) != null) {
                String[] words = line.split(" ");
                for (String word : words) file1Words.add(word.toLowerCase());
            }

        } catch (IOException e) {
            System.err.println("ERROR: Could not read file " + file1.getName() + " -- " + e.getMessage());
            System.exit(-1);
        }

        // read all words from file 2
        List<String> file2Words = new ArrayList<>();

        try (BufferedReader file2Stream = new BufferedReader(new FileReader(file2))) {
            
            String line;
            while ((line = file2Stream.readLine()) != null) {
                String[] words = line.split(" ");
                for (String word : words) file2Words.add(word.toLowerCase());
            }

        } catch (IOException e) {
            System.err.println("ERROR: Could not read file " + file2.getName() + " -- " + e.getMessage());
            System.exit(-1);
        }

        // compare the words
        List<String> similarities = getSimilarities(file1Words, file2Words, minimumMatchLength);

        // if there are no similarities found, report so.
        if (similarities.size() == 0) {
            System.out.println("\nNo similarities of length " + minimumMatchLength + " were found.\n\n");
        }
        else { // if there were similarities found, print them all

            System.out.println("\nFOUND " + similarities.size() + " SIMILARITIES.\n\n");

            for (String similarity : similarities) {

                System.out.println("Similarity found: " + similarity);

            }

            System.out.println();

        }

    }

    private static List<String> getSimilarities(List<String> words1, List<String> words2, int minimumMatchLength) {

        // store all matches in a list
        List<String> result = new ArrayList<>();

        // iterate over each word in the first file
        for (int p1 = 0; p1 < words1.size(); p1++) {

            // fetch the current word for convenience
            String word1 = words1.get(p1);

            // iterate over each word in the second fild
            for (int p2 = 0; p2 < words2.size(); p2++) {
                
                // fetch the current word for convenience
                String word2 = words2.get(p2);

                // if the two words match, we've found the start of potential match 
                if (word2.equals(word1)) {

                    // store temporary indices at the current location in both files
                    int temp1 = p1;
                    int temp2 = p2;

                    // store every word that matches in a list
                    List<String> matchWords = new ArrayList<>();

                                                                                // while:
                    while (temp1 < words1.size() &&                             // we haven't reached the end of file 1
                            temp2 < words2.size() &&                            // we haven't reached the end of file 2
                            words1.get(temp1++).equals(words2.get(temp2++))) {  // the two words we're looking at match (then we increment both indices),
                        matchWords.add(words1.get(temp1 - 1));                  // add the word we just incremented past to the match list
                    }

                    // if the amound of matching words is sufficient,
                    if (matchWords.size() >= minimumMatchLength) {
                        
                        // combine them all into one string and add it to the result list
                        result.add(combine(matchWords));

                    }

                    // since we've looked at all of these words, move both loops past them (and back by 1 to account for the loop increment at the end of this iteration)
                    p1 = temp1 - 1;
                    p2 = temp2 - 1;

                }

            }

        }

        return result;

    }

    private static String combine(List<String> words) {

        StringBuilder result = new StringBuilder();

        for (String word : words) {
            result.append(word).append(" ");
        }

        return result.toString().trim();

    }

}