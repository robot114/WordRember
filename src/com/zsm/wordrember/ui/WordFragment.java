package com.zsm.wordrember.ui;

import com.zsm.wordrember.R;
import com.zsm.wordrember.data.Book;
import com.zsm.wordrember.operator.OnBookUpdateListener;
import com.zsm.wordrember.operator.Operator;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public abstract class WordFragment extends Fragment implements OnBookUpdateListener {

	protected Operator mOperator;
	protected int mCurentChapterIndex;

	protected WordFragment( Operator o ) {
		mOperator = o;
	}
	
	@Override
	public void onBookUpdate(Book book) {
		mOperator.setBook(book);
		updateBook();
		selectCurrentChapter( book );
	}

	/**
	 * Select the chapter by user or by saved preference
	 */
	void selectCurrentChapter( final Book book ) {
		String[] titles = getChapterTitles(book);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder
			.setTitle( R.string.titleSelectChapter )
			.setItems(titles, null)
			.setOnItemSelectedListener( new OnItemSelectedListener() {
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					mCurentChapterIndex = 0;
				}
				
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					mCurentChapterIndex = position;
				}
			} )
			.setPositiveButton( android.R.string.ok, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mOperator.toChapter( mCurentChapterIndex );
					changeChapter();
				}
			})
			.setNegativeButton( android.R.string.cancel, null )
			.show();
	}
	
	private String[] getChapterTitles( final Book book ) {
		String[] titles = new String[book.getChapterCount()];
		for( int i = 0; i < titles.length; i++ ) {
			titles[i] = book.getChapter(i).getTitle();
		}
		
		return titles;
	}

	protected abstract void updateBook();
	protected abstract void changeChapter();

}
