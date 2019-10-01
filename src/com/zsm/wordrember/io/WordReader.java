package com.zsm.wordrember.io;

import java.io.IOException;

import com.zsm.wordrember.data.Word;

public interface WordReader {

	public static Word END_OF_CHAPTER = new Word( "EOC", "none",  "EndOfChapter" );
	public static Word CANCELLED = new Word( "CANCELLED", "none", "Cancel" );

	/**
	 * A word is written in one line in the following format:
	 * word|type( n., verb.,... )|description
	 * The description is a string with multi-ones separated by ";" or Chinese "£»"
	 * @param ind indicator to update process to the user
	 * 
	 * @return the word
	 * @throws IOException
	 */
	Word readNext(ProcessIndicator ind) throws IOException;

}