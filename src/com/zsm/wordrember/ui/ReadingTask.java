package com.zsm.wordrember.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.zsm.log.Log;
import com.zsm.wordrember.data.Book;
import com.zsm.wordrember.io.BookReader;
import com.zsm.wordrember.io.ProcessIndicator;
import com.zsm.wordrember.operator.OnBookUpdateListener;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.net.Uri;
import android.os.AsyncTask;

public class ReadingTask extends AsyncTask<Uri, String, Book>
							implements OnCancelListener, ProcessIndicator {
	
	private static final String MESSAGE_TYPE_CHAPTER = "CHAPTER";
	private static final String MESSAGE_TYPE_WORD = "WORD";
	
	private Context mContext;
	private OnBookUpdateListener mBookUpdateListener;
	
	private TextProgressDialog mDialog;
	
	private boolean mCancelled;

	ReadingTask( Context c, OnBookUpdateListener l ) {
		mContext = c;
		mBookUpdateListener = l;
	}

	@Override
	protected void onPreExecute() {
		mDialog = new TextProgressDialog( mContext, true, this );
		mDialog.show();
	}

	@Override
	protected void onPostExecute(Book result) {
		onCancelled(result);
	}

	@Override
	protected void onProgressUpdate(String... message) {
		switch( message[0] ) {
			case MESSAGE_TYPE_CHAPTER:
				mDialog.setChapter( message[1] );
				break;
			case MESSAGE_TYPE_WORD:
				mDialog.setMessage( message[1] );
				break;
			default:
				Log.w( "Invalid message type: ", (Object[])message );
				break;
		}
	}

	@Override
	protected void onCancelled( Book result ) {
		mDialog.dismiss();
		mBookUpdateListener.onBookUpdate(result);
	}

	@Override
	protected Book doInBackground(Uri... uri) {
		mCancelled = false;
		InputStream in;
		try {
			in = mContext.getContentResolver().openInputStream(uri[0]);
		} catch (FileNotFoundException e) {
			Log.e( e, "Failed to open: " + uri[0] );
			return null;
		}
		
		Reader reader = new InputStreamReader( in );
		
		BookReader br = new BookReader( );
		Book book = null;
		try {
			book = br.read( uri[0].getLastPathSegment(), reader, this );
		} catch (IOException e) {
			Log.e( e, "Read book failed!" );
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				Log.e( e, "Failed to close: " + uri[0] );
			}
		}
		
		return book;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		mCancelled = true;
	}

	@Override
	public boolean processChapter(String title) {
		publishProgress( MESSAGE_TYPE_CHAPTER, title );
		return !mCancelled;
	}

	@Override
	public boolean updateWord(String text) {
		publishProgress( MESSAGE_TYPE_WORD, text );
		return !mCancelled;
	}

}
