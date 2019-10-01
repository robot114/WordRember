package com.zsm.wordrember.data;

public class ChapterFactory {

	public static final int ORDER_INPUT = 0;
	public static final int ORDER_ALPHABET = 1;
	public static final int ORDER_RANDOM = 2;
	
	private static ChapterFactory mInstance;

	private ChapterFactory() {
	}
	
	public static ChapterFactory getInstance() {
		if( mInstance == null ) {
			mInstance = new ChapterFactory();
		}
		
		return mInstance;
	}
	
	public Chapter makeChapter( Chapter source, int orderMode ) {
		switch( orderMode ) {
			case ChapterFactory.ORDER_ALPHABET:
				return new AlphabetOrderChapter( source );
			case ChapterFactory.ORDER_RANDOM:
				return new RandomChapter( source );
			default:
				return source;
		}
	}
}
