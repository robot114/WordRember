package com.zsm.wordrember.data;

import java.util.Vector;

public class Chapter {
	
	public enum ORDER { DISORDER, IN_ORDER, RANDOM };

	private String mTitle;
	protected Vector<Word> mWordVector;
	private boolean mDuplicate;
	
	protected Chapter( Chapter c ) {
		this( c.mTitle, c.mDuplicate );
	}
	
	public Chapter( String title, boolean duplicate ) {
		mTitle = title;
		mDuplicate = duplicate;
		
		mWordVector = new Vector<Word>( 100 );
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public void add( Word w ) {
		mWordVector.add( w );
	}
	
	public Word get( int index ) {
		return mWordVector.get(index);
	}

	@SuppressWarnings("unchecked")
	protected Vector<Word> cloneWordsVector() {
		return (Vector<Word>) mWordVector.clone();
	}
	
	public int getWordCount( ) {
		return mWordVector.size();
	}

	public boolean contains(Word word) {
		return mWordVector.contains(word);
	}
}
