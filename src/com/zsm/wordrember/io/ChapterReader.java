package com.zsm.wordrember.io;

import java.io.IOException;

import com.zsm.wordrember.data.Chapter;
import com.zsm.wordrember.data.Word;

public abstract class ChapterReader {

	protected boolean mDupWord;
	private boolean mIsCancelled;

	public ChapterReader( boolean dupWord ) {
		mDupWord = dupWord;
		mIsCancelled = false;
	}

	public Chapter readNextChapter( ProcessIndicator ind ) throws IOException {
		
		String title = nextTitle();
		
		if( title == null ) {
			return null;
		}
		
		Chapter c = new Chapter(title, mDupWord );
		if( !ind.processChapter(title) ) {
			mIsCancelled = true;
			return c;
		}
		
		WordReader wr = getWordReader( );
		Word word;
		while( ( word = wr.readNext( ind ) ) != null 
				&& word !=WordReader.END_OF_CHAPTER && !isCancelled() ) {
			
			if( mDupWord || !c.contains( word ) ) {
				c.add( word );
			}
			if( word == WordReader.CANCELLED ) {
				mIsCancelled = true;
				break;
			}
		}
		
		return c;
	}
	
	public boolean isCancelled() {
		return mIsCancelled;
	}

	abstract protected String nextTitle() throws IOException;
	abstract protected WordReader getWordReader();
}