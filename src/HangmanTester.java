import java.util.Arrays;

public class HangmanTester {

    private static int correctTests = 0;
    private static int totalTests = 0;

    /** Clear test count variables */
    private static void clearCounts() {
        correctTests = 0;
        totalTests = 0;
    }

    /**
     * Update test count variables depending on if test passed.
     * @param correct True if test counts as correct.
     */
    private static void countTest(boolean correct) {
        if(correct) {
            correctTests++;
        }
        totalTests++;
    }

    /**
     * Print the testing results.
     * @param methodName The name of the method tested.
     */
    private static void printResults(String methodName) {
        String msg = "";
        if(correctTests < totalTests) {
            // I want failed tests to really jump out at you.
            msg = " INCORRECT!";
        }
        System.out.println("testing " + methodName + ": passes " +
                correctTests + " of " + totalTests + " tests" + msg);
    }

    private static void testHasUnknown() {
        // hasUnknown() should only return false if the char[] passed to it
        // contains no blanks
        // it should not change the array

        String[] incompleteStrs = { "*oobar", "foo*ar", "fooba*", "*", "f**ba*"};
        for(String str : incompleteStrs) {
            char[] arr = str.toCharArray();
            countTest(Hangman.hasUnknown(arr));
            countTest(Arrays.equals(arr, str.toCharArray()));
        }

        String str = "foobar";
        char[] arr = str.toCharArray();
        countTest(!Hangman.hasUnknown(arr));
        countTest(Arrays.equals(arr, str.toCharArray()));
    }

    private static void testUpdateWithGuess() {
        String word = "foobar";
        char[] known = "******".toCharArray();
        countTest(!Hangman.updateWithGuess(word, known, 'e'));
        countTest(Arrays.equals("******".toCharArray(), known));
        // When the letter is present, updateWithGuess() should return true
        countTest(Hangman.updateWithGuess(word, known, 'o'));
        // updateWithGuess() should also modify the char[] that was passed in
        countTest(Arrays.equals("*oo***".toCharArray(), known));
        countTest(!Hangman.updateWithGuess(word, known, 't'));
        countTest(Arrays.equals("*oo***".toCharArray(), known));
        countTest(Hangman.updateWithGuess(word, known, 'f'));
        countTest(Arrays.equals("foo***".toCharArray(), known));
        countTest(!Hangman.updateWithGuess(word, known, 'z'));
        countTest(Arrays.equals("foo***".toCharArray(), known));
        countTest(Hangman.updateWithGuess(word, known, 'r'));
        countTest(Arrays.equals("foo**r".toCharArray(), known));

        word = "banana";
        known = "******".toCharArray();
        countTest(Hangman.updateWithGuess(word, known, 'a'));
        countTest(Arrays.equals("*a*a*a".toCharArray(), known));
        countTest(!Hangman.updateWithGuess(word, known, 'e'));
        countTest(Arrays.equals("*a*a*a".toCharArray(), known));
        countTest(Hangman.updateWithGuess(word, known, 'b'));
        countTest(Arrays.equals("ba*a*a".toCharArray(), known));
        countTest(Hangman.updateWithGuess(word, known, 'n'));
        countTest(Arrays.equals(word.toCharArray(), known));

    }

    private static void testSelectRandomWord() {
        // Make a tiny dictionary
        String[] dictionary = { "foo", "bar", "baz", "qux", "quux" };
        // Sort so I'll be able to use binarySearch
        Arrays.sort(dictionary);

        int[] counts = new int[dictionary.length];
        int n = 10000;
        // Many times, choose a random word from this dictionary
        for (int i = 0; i < n; i++) {
            String word = Hangman.selectRandomWord(dictionary);
            // Word selected better be in the dictionary
            int index = word == null ? -1 : Arrays.binarySearch(dictionary, word);
            countTest(index >= 0);
            // Update a counter for each word when it appears
            if(index >= 0) { counts[index]++; }
        }
        // All words in the dictionary should appear with similar frequency
        double expected = (double)n / dictionary.length;
        for (int i = 0; i < counts.length; i++) {
            countTest(counts[i] > expected * 0.9);
            countTest(counts[i] < expected * 1.1);
        }
    }

    public static void main(String[] args) {
        clearCounts();
        testHasUnknown();
        printResults("hasUnknown");

        clearCounts();
        testUpdateWithGuess();
        printResults("updateWithGuess");

        clearCounts();
        testSelectRandomWord();
        printResults("selectRandomWord");
    }
}