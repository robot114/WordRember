package com.zsm.wordrember.operator;

import java.util.Arrays;

import com.zsm.wordrember.data.Book;
import com.zsm.wordrember.data.Word;

public abstract class Operator implements BookKeeper {

	public enum NEXT_WORD_RESULT { MORE_WORD, NO_MORE_WORD_IN_CHAPTER, NO_MORE_CHAPTER };
	
	public static final int LANG_CH_EN = 0;
	public static final int LANG_EN_CH = 1;
	public static final int LANG_RANDOM = 2;

	private BookKeeper mGlobalKeeper;
	private int mDisplayLangMode;

	protected Operator( BookKeeper globalKeeper ) {
		mGlobalKeeper = globalKeeper;
	}
	
	@Override
	public void setBook(Book book) {
		mGlobalKeeper.setBook(book);
	}
	
	@Override
	public Book getBook() {
		return mGlobalKeeper.getBook();
	}

	public void setDisplayLangMode(int mode) {
		mDisplayLangMode = mode;
	}
	
	public boolean check( String text ) {
		if( mDisplayLangMode == LANG_EN_CH ) {
			return current().checkMeanings(text, false);
		} else {
			return current().checkWord(text, false);
		}
	}
	
	public String hint( String currentHint ) {
		String text;
		
		if( mDisplayLangMode == LANG_EN_CH ) {
			text = current().getMeaningsString();
		} else {
			text = current().getWord();
		}
		
		final int length = text.length();
		
		char[] chars = new char[ length ];
		Arrays.fill(chars, ' ');
		
		if( currentHint.length() != length ) {
			currentHint = new String( chars );
		}

		int[] poses = new int[length];
		int posLen = 0;
		for( int i = 0; i < length; i++ ) {
			final char charAt = text.charAt(i);
			if( currentHint.charAt(i) == charAt || isDelimeter(charAt) ) {
				chars[i] = charAt;
			} else {
				chars[i] = ' ';
				poses[posLen++] = i;
			}
		}
		
		if( posLen == 0 ) {
			return text;
		}
		
		int pos = (int) (Math.random()*posLen);
		chars[poses[pos]] = text.charAt(poses[pos]);
		
		return new String(chars);
	}

	private boolean isDelimeter(final char charAt) {
		return charAt == ';' || charAt == Word.CHN_DELIMETER;
	}

	/**
	 * Reset the word to the first one of the book
	 */
	public abstract void resetBook();
	
	/**
	 * Reset the word to the first one of the chapter
	 * @throws IndexOutOfBoundsException if the current chapter index beyonds the last one
	 */
	public abstract void resetChapter() throws IndexOutOfBoundsException;
	
	/**
	 * Set the current chapter and move the word index to the first one
	 * 
	 * @param chapterIndex
	 * @throws IndexOutOfBoundsException if chapterIndex beyonds the last one
	 */
	public abstract void toChapter( int chapterIndex ) throws IndexOutOfBoundsException;
	
	/**
	 * To the next chapter and move the word index to the first one
	 * 
	 */
	public abstract NEXT_WORD_RESULT nextChapter( );
	
	/**
	 * Return the current word. If the pointer of the word beyond the bounds,
	 * a IndexOutOfBoundsException will be thrown. So this method should not be
	 * invoked after the method {@link next} return false, and no resetXX method
	 * is invoked
	 * 
	 * @return the current word
	 * @throws IndexOutOfBoundsException
	 */
	public abstract Word current() throws IndexOutOfBoundsException;
	
	/**
	 * Next word
	 * 
	 * @return 
	 */
	public abstract NEXT_WORD_RESULT nextWord( );
	
}
