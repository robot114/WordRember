package com.zsm.wordrember.app;

import com.zsm.log.Log;
import com.zsm.wordrember.data.ChapterFactory;
import com.zsm.wordrember.operator.Operator;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceManager;

public class Preferences {

	private static final String KEY_NEW_ORDER_MODE = "KEY_NEW_ORDER_MODE";

	private static final String KEY_NEW_LANG_MODE = "KEY_NEW_LANG_MODE";

	private static final String KEY_CURRENT_LOAD_PATH = "KEY_CURRENT_LOAD_PATH";

	private StackTraceElement[] stackTrace;
	
	private static Preferences mInstance;
	private SharedPreferences mPreferences;

	private Preferences( Context c ) {
		mPreferences = PreferenceManager.getDefaultSharedPreferences( c );
	}
	
	public static void init(Context c) {
		if( mInstance != null ) {
			throw new IllegalStateException( "Preference has been initialized! "
											 + "Call getInitStackTrace() to get "
											 + "the initlization place." );
		}
		
		mInstance = new Preferences( c );
		mInstance.stackTrace = Thread.currentThread().getStackTrace();
	}
	
	public static Preferences getInstance() {
		return mInstance;
	}

	public StackTraceElement[] getInitStackTrace() {
		return stackTrace;
	}

	public void setCurrentBookUri(Uri uri) {
		mPreferences.edit().putString( KEY_CURRENT_LOAD_PATH, uri.toString() ).commit();
	}
	
	public Uri getCurrentBookUri() {
		String text = mPreferences.getString( KEY_CURRENT_LOAD_PATH, null );
		if( text == null ) {
			return null;
		}
		
		try {
			Uri uri = Uri.parse(text);
			return uri;
		} catch ( Exception e ) {
			Log.w( e, "Load KEY_CURRENT_LOAD_PATH failed!" );
			return null;
		}
	}

	public void setNewLangMode(int langMode) {
		mPreferences.edit().putInt( KEY_NEW_LANG_MODE, langMode ).commit();
	}
	
	public int getNewLangMode() {
		return mPreferences.getInt( KEY_NEW_LANG_MODE, Operator.LANG_CH_EN );
	}

	public void setNewOrderMode(int orderMode) {
		mPreferences.edit().putInt( KEY_NEW_ORDER_MODE, orderMode ).commit();
	}

	public int getNewOrderMode() {
		return mPreferences.getInt( KEY_NEW_ORDER_MODE, ChapterFactory.ORDER_INPUT );
	}
}
