package com.zsm.wordrember.io;

import java.io.IOException;
import java.io.Reader;

import com.zsm.wordrember.data.Book;
import com.zsm.wordrember.data.Chapter;

public class BookReader {

	public Book read( String name, Reader r, ProcessIndicator ind ) throws IOException {
		Book b = new Book( name );
		
		Chapter c;
		TextChapterReader cr = new TextChapterReader( r, false );
		
		while( (c = cr.readNextChapter( ind )) != null && !cr.isCancelled() ) {
			b.addChapter(c);
		}
		
		return b;
	}
}
