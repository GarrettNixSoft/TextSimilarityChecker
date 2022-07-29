# TextSimilarityChecker
A basic tool for finding matching word sequences in text files.

Usage:

Compile with: `javac TextChecker.java`

Store both files you want to compare as .txt files.

Run with: `java TextChecker <file 1> <file 2> [minimum match length]`

The application will search both files for any sequence of words that match exactly with the minimum length specified (default 3). It does not report where in the document the matches are found -- I recommend using `Ctrl + F` to find where they are.
