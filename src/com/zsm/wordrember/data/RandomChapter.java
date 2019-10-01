package com.zsm.wordrember.data;

import java.util.Vector;

public class RandomChapter extends Chapter {

	protected RandomChapter(Chapter c) {
		super(c);
		
		// Keep the order of the ordinary chapter
		mWordVector = (Vector<Word>) c.cloneWordsVector();
		random( c );
	}
	
	private void random( Chapter c ) {
		int last = mWordVector.size()-1;
		while( last > 1 ) {
			int rand = (int)( Math.random() * last );
			
			Word w = mWordVector.get(rand);
			mWordVector.set(rand, mWordVector.get(last));
			mWordVector.set( last, w );
			
			last--;
		}
	}
}
