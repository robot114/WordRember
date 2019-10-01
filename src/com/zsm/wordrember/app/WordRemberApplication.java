package com.zsm.wordrember.app;

import com.zsm.driver.android.log.LogInstaller;
import com.zsm.driver.android.log.LogPreferences;
import com.zsm.log.Log;
import com.zsm.wordrember.data.Book;
import com.zsm.wordrember.operator.BookKeeper;

import android.app.Application;

public class WordRemberApplication extends Application implements BookKeeper {

	private Book mBook;

	public WordRemberApplication() {
		LogInstaller.installAndroidLog( "WordRember" );
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		LogPreferences.init( this );
		LogInstaller.installFileLog( this );
		Log.setGlobalLevel( Log.LEVEL.DEBUG );
		
		Preferences.init( this );
	}

	@Override
	public void setBook(Book book) {
		mBook = book;
	}

	@Override
	public Book getBook() {
		return mBook;
	}

}
