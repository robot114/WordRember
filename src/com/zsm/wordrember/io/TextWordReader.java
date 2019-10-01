package com.zsm.wordrember.io;

import java.io.BufferedReader;
import java.io.IOException;

import com.zsm.wordrember.data.Word;

public class TextWordReader implements WordReader {
	
	private static final char PART_DELIM = '|';
	private BufferedReader mReader;

	public TextWordReader(BufferedReader r) {
		mReader = r;
	}

	/* (non-Javadoc)
	 * @see com.zsm.wordrember.io.WordReader#readOne(java.io.BufferedReader)
	 */
	@Override
	public Word readNext(ProcessIndicator ind) throws IOException {
		while( true ) {
			String text = TextIOUtil.skipEmptyLine(mReader);
			
			if( text == null ) {
				return null;
			}
			
			if( text.startsWith( "====" ) ) {
				return END_OF_CHAPTER;
			}
			
			if( !ind.updateWord(text) ) {
				return CANCELLED;
			}
			
			int index = text.indexOf( PART_DELIM );
			if( index <= 0 || index >= ( text.length() - 1 ) ) {
				// No word or just word
				continue;
			}
			String word = text.substring(0, index).trim();
			
			int start = index + 1;
			index = text.indexOf(PART_DELIM, start);
			if( index < 0 || ( index >= text.length() - 1 ) ) {
				// no meanings
				continue;
			}
			String type = text.substring(start, index).trim();
			
			start = index + 1;
			index = text.indexOf( PART_DELIM, start );
			if( index > 0 ) {
				// more content
				continue;
			}
			String meanings = text.substring(start).trim();
			
			if( word.length() == 0 || meanings.length() == 0 ) {
				// invalid format line
				continue;
			}
			
			Word newWord = new Word( word, type, meanings );
			if( newWord.getMeanings().size() == 0 ) {
				// no meanings
				continue;
			}
			
			return newWord;
		}
	}
}
