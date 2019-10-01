package com.zsm.wordrember.operator;

import java.util.Arrays;

import com.zsm.wordrember.data.Chapter;
import com.zsm.wordrember.data.ChapterFactory;
import com.zsm.wordrember.data.Word;

public class NewOperator extends Operator {

	private Chapter[] mCurrnetChapter = new Chapter[ChapterFactory.ORDER_RANDOM+1];
	
	private int mChapterIndex;
	private int mWordIndex;
	private int mOrderMode;

	public NewOperator(BookKeeper globalKeeper) {
		super(globalKeeper);
	}

	@Override
	public void resetBook() {
		toChapter(0);
	}

	@Override
	public void resetChapter() throws IndexOutOfBoundsException {
		checkChapterIndex(mChapterIndex);
		
		mWordIndex = 0;
	}

	private void checkChapterIndex(int chapterIndex) {
		if( isChapterOutOfBounds(chapterIndex) ) {
			throw new IndexOutOfBoundsException( "Invalid chapter index: " + chapterIndex );
		}
	}

	private boolean isChapterOutOfBounds(int chapterIndex) {
		return chapterIndex < 0 || chapterIndex >= getBook().getChapterCount();
	}

	@Override
	public void toChapter(int chapterIndex) throws IndexOutOfBoundsException {
		checkChapterIndex(chapterIndex);
		mChapterIndex = chapterIndex;
		mWordIndex = 0;

		// reset random and alphabet chapter
		Arrays.fill(mCurrnetChapter, null);
		mCurrnetChapter[ChapterFactory.ORDER_INPUT] = getBook().getChapter(chapterIndex);
		changeOrderMode( mOrderMode );
	}

	@Override
	public NEXT_WORD_RESULT nextChapter() {
		final int nextChapter = mChapterIndex + 1;
		if( nextChapter >= getBook().getChapterCount() ) {
			return NEXT_WORD_RESULT.NO_MORE_CHAPTER;
		} else {
			toChapter(nextChapter);
			return NEXT_WORD_RESULT.MORE_WORD;
		}
	}

	@Override
	public Word current() {
		checkChapterIndex(mChapterIndex);
		checkWordIndex();

		return getCurrentChapter( ).get(mWordIndex);
	}

	private Chapter getCurrentChapter( ) {
		return mCurrnetChapter[mOrderMode];
	}
	
	private void checkWordIndex() {
		if( isWordIndexOutOfBounds() ) {
			throw new IndexOutOfBoundsException( "Invalid word index: " + mWordIndex );
		}
	}

	private boolean isWordIndexOutOfBounds() {
		return mWordIndex < 0 || mWordIndex >= getBook().getChapter(mChapterIndex).getWordCount();
	}

	@Override
	public NEXT_WORD_RESULT nextWord() {
		mWordIndex++;
		if( !isWordIndexOutOfBounds() ) {
			return NEXT_WORD_RESULT.MORE_WORD;
		} else {
			final int chapterIndex = mChapterIndex+1;
			if( chapterIndex < getBook().getChapterCount() ) {
				return NEXT_WORD_RESULT.NO_MORE_WORD_IN_CHAPTER;
			} else {
				return NEXT_WORD_RESULT.NO_MORE_CHAPTER;
			}
		}
	}

	public void changeOrderMode(int orderMode) {
		if( mCurrnetChapter[ChapterFactory.ORDER_INPUT] == null ) {
			return;
		}
		mOrderMode = orderMode;
		if( mCurrnetChapter[orderMode] == null ) {
			mCurrnetChapter[orderMode]
				= ChapterFactory.getInstance()
					.makeChapter(mCurrnetChapter[ChapterFactory.ORDER_INPUT],
								 orderMode);
		}
	}
}
