package com.zsm.wordrember.ui;

import com.zsm.log.Log;
import com.zsm.wordrember.R;
import com.zsm.wordrember.app.Preferences;
import com.zsm.wordrember.data.ChapterFactory;
import com.zsm.wordrember.data.Word;
import com.zsm.wordrember.operator.BookKeeper;
import com.zsm.wordrember.operator.NewOperator;
import com.zsm.wordrember.operator.Operator;
import com.zsm.wordrember.operator.Operator.NEXT_WORD_RESULT;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class NewFragment extends WordFragment {

	private static final int[] LANG_LABEL
		= { R.string.labelChToEn, R.string.labelEnToCh, R.string.labelLangRandom };
	private static final int[] ORDER_LABEL
		= { R.string.labelOrderInupt, R.string.labelOrderAlphabet, R.string.labelOrderRandom };
			
	private int mLangMode = Operator.LANG_CH_EN;
	private int mOrderMode = ChapterFactory.ORDER_INPUT;
	private TextView mTextView;
	private EditText mEditView;
	
	protected NewFragment(BookKeeper bk) {
		super( new NewOperator( bk ) );
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.word_fragment, container, false);
		View viewLang = rootView.findViewById( R.id.buttonChangeLang );
		updateLangMode(viewLang, mLangMode);
		viewLang.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateLangMode(v, mLangMode+1);
				Preferences.getInstance().setNewLangMode( mLangMode );
			}
		} );
		updateLangMode(viewLang, Preferences.getInstance().getNewLangMode());

		View viewOrder = rootView.findViewById( R.id.buttonOrder );
		updateOrderMode(viewOrder, mOrderMode);
		viewOrder.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateOrderMode(v, mOrderMode+1);
				Preferences.getInstance().setNewOrderMode( mOrderMode );
			}
		} );
		updateOrderMode(viewOrder, Preferences.getInstance().getNewOrderMode());
		
		mTextView = (TextView)rootView.findViewById( R.id.textView );
		mTextView.setText("");
		mEditView = (EditText)rootView.findViewById(R.id.editText);
		mEditView.setText("");
		
		View refresh = rootView.findViewById( R.id.buttoRefresh );
		refresh.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				displayWord();
			}
		} );
		
		View next = rootView.findViewById( R.id.imageRight );
		next.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				nextWord();
			}
		} );
		
		final TextView result = (TextView)rootView.findViewById( R.id.textViewResult );
		result.setText("");
		
		View check = rootView.findViewById( R.id.buttonCheck );
		check.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				if( mOperator.check( mEditView.getText().toString().trim() ) ) {
					publishResult(R.color.result_right, R.string.resultRight);
					nextWord();
				} else {
					publishResult( R.color.result_wrong, R.string.resultWrong );
				}
			}

			private void publishResult(int textColor, int textResId) {
				int color = getActivity().getResources().getColor( textColor );
				result.setTextColor( color );
				result.setText( textResId );
				new Handler( Looper.getMainLooper() ).postDelayed( new Runnable() {
					@Override
					public void run() {
						result.setText("");
					} 
				}, 5000 );
			}
		} );
		
		final View hintView = rootView.findViewById( R.id.buttonHint );
		hintView.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				mEditView.setText( mOperator.hint( mEditView.getText().toString() ) );
			}
		} );
		return rootView;
	}

	@Override
	protected void updateBook() {
		mOperator.resetBook();
	}

	@Override
	protected void changeChapter() {
		((NewOperator)mOperator).changeOrderMode( mOrderMode );
		displayWord();
	}
	
	private void displayWord() {
		mEditView.setText("");
		Word word = mOperator.current();
		int mode;
		if( mLangMode == Operator.LANG_RANDOM ) {
			mode = (int)(Math.random() * 2);
		} else {
			mode = mLangMode;
		}
		mOperator.setDisplayLangMode( mode );
		
		switch( mode ) {
			case Operator.LANG_CH_EN:
				mTextView.setText( word.getMeaningsString() );
				// TODO: Do not work
				mEditView.setInputType(
						InputType.TYPE_CLASS_TEXT
						|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
				break;
			case Operator.LANG_EN_CH:
				mTextView.setText( word.getWord() );
				// TODO: Do not work
				mEditView.setInputType(
						InputType.TYPE_CLASS_TEXT
						|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
						|InputType.TYPE_TEXT_VARIATION_PHONETIC);
				break;
			default:
				Log.w( "Invalid langue mode", mode );
				return;
		}
	}

	private void updateOrderMode(View v, int orderMode) {
		orderMode = orderMode%ORDER_LABEL.length;
		((TextView)v).setText( ORDER_LABEL[orderMode] );
		((NewOperator)mOperator).changeOrderMode( orderMode );
		mOrderMode = orderMode;
	}

	private void updateLangMode(View v, int langMode) {
		langMode = langMode % LANG_LABEL.length;
		((TextView)v).setText( LANG_LABEL[langMode] );
		mLangMode = langMode;
	}

	private void nextWord() {
		NEXT_WORD_RESULT res = mOperator.nextWord();
		switch( res ) {
			case MORE_WORD:
				displayWord();
				break;
			case NO_MORE_WORD_IN_CHAPTER:
				noMoreWordInChapter();
				break;
			case NO_MORE_CHAPTER:
				noMoreChapter();
				break;
			default:
				Log.w( "Invalid result of nextWord", res );
				break;
		}
	}

	private void noMoreWordInChapter() {
		new AlertDialog.Builder( getActivity() )
			.setIcon( android.R.drawable.ic_dialog_info )
			.setMessage( R.string.msgNoMoreWordInChapter )
			.setPositiveButton( R.string.titleThisChapterAgain,
								new  DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mOperator.resetChapter();
					displayWord();
				}
			})
			.setNegativeButton( R.string.titleNextChapter, 
							   new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mOperator.nextChapter();
					displayWord();
				}
			})
			.show();
	}

	private void noMoreChapter() {
		new AlertDialog.Builder( getActivity() )
			.setIcon( android.R.drawable.ic_dialog_info )
			.setMessage( R.string.msgNoMoreChapter )
			.setPositiveButton( R.string.titleThisChapterAgain,
								new  DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mOperator.resetChapter();
					displayWord();
				}
			})
			.setNegativeButton( R.string.titleThisBookAgain, 
							   new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mOperator.resetBook();
					displayWord();
				}
			})
			.show();
	}

}
