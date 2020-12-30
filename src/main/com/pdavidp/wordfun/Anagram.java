package main.com.pdavidp.wordfun;

import java.io.IOException;

class Anagram {

    private static final int NUMBEROFROWS = 5;
    private static final int NUMBEROFCOLUMNS = 5;
    private static final String WORD_NOT_FOUND = "Your word is not in the remaining letters.";
    private static final String SHOWING_FROM_TO = "Showing words %s to %s of %s.";
    private boolean resetLastOption, complete;
    private int currentLetterCount;
    private Word remainingLetters = new Word();
    private WordPool availableOptions = new WordPool();
    private WordPool wordsSelected = new WordPool();

    Anagram(Word input) throws IOException {
        remainingLetters.setLetters(input.getLetters().replaceAll(" ", ""));
        complete = false;
        availableOptions.buildDictionary();
        availableOptions.filterList(remainingLetters);
        currentLetterCount = 0;
        resetLastOption = true;
    }

    /**
     * Display a list of options available
     *
     * @param currentWord 1 based index showing user options
     */
    int displayOptions(int currentWord) {
        // reset the last option back to zero since a word was selected
        int showTo;
        if (resetLastOption) {
            currentLetterCount = 0;
            resetLastOption = false;
        } else {
            // Add one so you don't show the last option twice (ie 1-25, and 25-50)
            currentLetterCount = currentWord + 1;
        }

        final int pageSize = NUMBEROFCOLUMNS*NUMBEROFROWS - 1;
        final int listSize = availableOptions.getList().size();
        if (listSize < currentLetterCount + pageSize) {
            // nearing the end of the list
            showTo = listSize;
            System.out.printf(SHOWING_FROM_TO + "\n", "1", showTo, listSize);
        } else {
            showTo = currentLetterCount + pageSize;
            System.out.printf(SHOWING_FROM_TO + " Enter \".\" to show more.\n", currentLetterCount + 1, showTo + 1, listSize);
        }

        WordPool optionsToShow = availableOptions.getWordPool(currentLetterCount,showTo);
        optionsToShow.showList(NUMBEROFCOLUMNS);

        return showTo;
    }


    void selectWord(String userEntry) {
        Word selection = new Word(userEntry);
        if (selection.isASubsetOf(remainingLetters)) {
            remainingLetters.removeLetters(selection.getLetters());
            availableOptions.filterList(remainingLetters);
            wordsSelected.add(selection);
        } else {
            System.out.println(WORD_NOT_FOUND);
        }
        resetLastOption = true;

        if (remainingLetters.length()==0) {
            complete = true;
        }
    }

    void showSelected() {
        StringBuilder allWords = new StringBuilder();
        for (Word word : wordsSelected.getList()) {
            allWords.append(word.getLetters()).append("  ");
        }
        System.out.println("Words selected: " + allWords);
    }

    boolean isComplete() {
        return complete;
    }

    void showRemainingLetters() {
        if (remainingLetters.length() > 0 ) {
            System.out.println("Letters remaining: " + remainingLetters.getLetters());
        }
    }
}