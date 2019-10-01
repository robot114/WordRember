package com.zsm.wordrember.io;

import java.io.BufferedReader;
import java.io.IOException;

import android.text.TextUtils;

public class TextIOUtil {

	private TextIOUtil() {
	}
	
	static String skipEmptyLine(BufferedReader br) throws IOException {
		String text;
		while( ( text = br.readLine() ) != null ) {
			text = text.trim();
			if( !TextUtils.isEmpty(text) ) {
				break;
			}
		}
		
		return text;
	}

}
