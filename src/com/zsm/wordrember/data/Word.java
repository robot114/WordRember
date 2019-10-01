package com.zsm.wordrember.data;

import java.util.Comparator;
import java.util.Vector;

public class Word {

	public static final char CHN_DELIMETER = '£»';

	public enum TYPE { noun, verb, adjective, adverb, pronoun, determiner, interjection };
	
	private String mWord;
	private String mType;
	private Vector<String> mMeanings;
	private String mMeaningsString;
	
	private static Comparator<Word> caseSensitiveComparator = new Comparator<Word>() {
		@Override
		public int compare(Word w1, Word w2) {
			return w1.mWord.compareTo(w2.mWord);
		}
	};

	private static Comparator<Word> caseInsensitiveComparator = new Comparator<Word>() {
		@Override
		public int compare(Word w1, Word w2) {
			return w1.mWord.compareToIgnoreCase(w2.mWord);
		}
	};

	public Word( String word, String type, Vector<String> meanings ) {
		mWord = word;
		mType = type;
		mMeanings = meanings;
		mMeaningsString = buildMeaningsString(meanings);
	}
	
	public Word( String word, String type, String meanings ) {
		mWord = word;
		mType = type;
		mMeanings = separateMeanings(meanings);
		mMeaningsString = meanings;
	}
	
	public String getWord() {
		return mWord;
	}
	
	public String getType() {
		return mType;
	}
	
	public Vector<String> getMeanings() {
		return mMeanings;
	}
	
	public boolean checkWord( String word, boolean caseSensitive ) {
		return caseSensitive ? mWord.equals(word) : mWord.equalsIgnoreCase(word);
	}
	
	/**
	 * Check the meanings of the word
	 * @param meagings delimited by ";" or "£»" 
	 * @param strictly true, the meanings MUST match exactly those of the word, 
	 * 					but the order is not matter. false, the meanings are
	 * 					part of those of the word
	 * @return
	 */
	public boolean checkMeanings( String meanings, boolean strictly ) {
		Vector<String> v = separateMeanings(meanings);
		if( v == null ) {
			return false;
		}
		
		if( strictly ) {
			return mMeanings.containsAll(v) && v.containsAll(mMeanings);
		} else {
			return mMeanings.containsAll(v);
		}
	}
	
	public static Comparator<Word> getComparatorByAlphabet( boolean caseSensitive ) {
		return caseSensitive ? caseSensitiveComparator : caseInsensitiveComparator;
	}

	private Vector<String> separateMeanings(String meanings) {
		Vector<String> v = new Vector<String>( 10 );
		for( int start = 0, end = 0; start < meanings.length(); end++ ) {
			if( end == meanings.length() ) {
				v.add( meanings.substring(start, end) );
				break;
			}
			char c = meanings.charAt(end); 
			if( c == ';' || c == CHN_DELIMETER )  {
				if( start < end ) {
					v.add( meanings.substring(start, end) );
				}
				start = end + 1;
			}
		}
		return v;
	}

	private String buildMeaningsString( Vector<String> meanings ) {
		StringBuilder builder = new StringBuilder();
		int count = meanings.size();
		
		for( int i = 0; i < count - 1; i++ ) {
			builder.append( meanings.get(i) ).append( CHN_DELIMETER );
		}
		
		builder.append( meanings.get(count-1) );
		
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj == this ) {
			return true;
		}
		
		if( obj == null ) {
			return false;
		}
		
		if( !( obj instanceof Word ) ) {
			return false;
		}
		
		Word w = (Word)obj;
		
		return w.mWord == mWord && w.mType == mType;
	}

	public String getMeaningsString() {
		return mMeaningsString;
	}

}
