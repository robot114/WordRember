package com.zsm.wordrember.data;

import java.util.Vector;

public class Book {

	private String mName;
	private Vector<Chapter> mChapters;

	public Book( String name ) {
		mName = name;
		mChapters = new Vector<Chapter>();
	}
	
	public String getName() {
		return mName;
	}
	
	/**
	 * Add on chapter into the book. The order of the chapters in the book is
	 * the one it is added
	 * 
	 * @param c the chapter
	 */
	public void addChapter( Chapter c ) {
		mChapters.add( c );
	}
	
	public Chapter getChapter( int index ) {
		return mChapters.get(index);
	}
	
	public int getChapterCount() {
		return mChapters.size();
	}

	public int getWordCount() {
		int count = 0;
		for( Chapter c : mChapters ) {
			count += c.getWordCount();
		}
		return count;
	}
}
