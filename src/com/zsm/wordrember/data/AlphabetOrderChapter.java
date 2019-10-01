package com.zsm.wordrember.data;

import java.util.Collections;

public class AlphabetOrderChapter extends Chapter {

	protected AlphabetOrderChapter(Chapter c) {
		super( c );
		
		// Keep the order of the ordinary chapter
		mWordVector = c.cloneWordsVector();
		sortByAlphabet(false);
	}

	private void sortByAlphabet( boolean caseSensitive ) {
		Collections.sort(mWordVector, Word.getComparatorByAlphabet(caseSensitive));
	}
	
}
