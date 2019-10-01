package com.zsm.wordrember.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class TextChapterReader extends ChapterReader {

	BufferedReader mReader;
	public TextChapterReader( Reader r, boolean dupWord ) {
		super( dupWord );
		if( r instanceof BufferedReader ) {
			mReader = (BufferedReader)r;
		} else {
			mReader = new BufferedReader( r );
		}
	}
	
	@Override
	protected String nextTitle() throws IOException {
		return TextIOUtil.skipEmptyLine(mReader);
	}
	@Override
	protected WordReader getWordReader() {
		return new TextWordReader( mReader );
	}
}
