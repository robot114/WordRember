package com.zsm.wordrember.ui;


import com.zsm.wordrember.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class TextProgressDialog extends AlertDialog implements OnClickListener {

	private TextView mMessageView;
	private TextView mChapter;
	private OnCancelListener mCancelListener;

	TextProgressDialog(Context context, boolean cancelable,
								 OnCancelListener cancelListener) {
		
		super(context, cancelable, cancelListener);
		mCancelListener = cancelListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        
        View view = inflater.inflate(R.layout.progress_dialog, (ViewGroup)null);
        mChapter = (TextView) view.findViewById( R.id.chapter );
        mMessageView = (TextView) view.findViewById(R.id.message);
        setView(view);
        setButton(BUTTON_NEGATIVE, getContext().getText( android.R.string.cancel ), this);
	}
	
	@Override
	public void setMessage(CharSequence message) {
		mMessageView.setText(message);
	}
	
	public void setChapter( CharSequence chapter ) {
		mChapter.setText(chapter);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		mCancelListener.onCancel(dialog);
	}

}
