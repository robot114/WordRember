package com.zsm.wordrember.ui;

import com.zsm.android.ui.documentSelector.DocumentHandler;
import com.zsm.android.ui.documentSelector.DocumentOperation;
import com.zsm.android.ui.documentSelector.DocumentSelector;
import com.zsm.util.file.FileExtensionFilter;
import com.zsm.util.file.FileUtilities;
import com.zsm.wordrember.R;
import com.zsm.wordrember.app.Preferences;
import com.zsm.wordrember.app.WordRemberApplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.provider.DocumentFile;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity 
			implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	private WordFragment mFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment
			.setUp( R.id.navigation_drawer,
					(DrawerLayout) findViewById(R.id.drawer_layout) );
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		final WordRemberApplication application
					= (WordRemberApplication)getApplication();
		
		mFragment = new NewFragment( application );
		if( application.getBook() == null ) {
			Uri bookUri = Preferences.getInstance().getCurrentBookUri(); 
			if( bookUri == null 
				|| !FileUtilities.doesFileExist( getContentResolver(), bookUri ) ) {
				
				choiceFile();
			} else {
				startToReadBook(bookUri);
			}
		} else {
			mFragment.onBookUpdate( application.getBook() );
		}
		
		fragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section_new);
			break;
		case 2:
			mTitle = getString(R.string.title_section_test);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onLoadFile( MenuItem item ) {
		choiceFile();
	}

	private void choiceFile() {
		FileExtensionFilter[] fef = new FileExtensionFilter[] { 
				new FileExtensionFilter( ".txt", "Text file" ) };
		
		Uri currentPath = Preferences.getInstance().getCurrentBookUri();
		
		DocumentSelector documentSelector
			= new DocumentSelector( this, DocumentOperation.LOAD, currentPath,
						new DocumentHandler() {

				@Override
				public void handleDocument(DocumentOperation operation,
										   DocumentFile document, String name) {

					final Uri uri = document.getUri();
					Preferences.getInstance().setCurrentBookUri( uri );
					startToReadBook(uri);
				}
				
			}, fef );
		
		documentSelector.show();
	}

	private void startToReadBook(Uri uri) {
		new ReadingTask(MainActivity.this, mFragment ).execute( uri );
	}
	
	public void onSelectChapter( MenuItem item ) {
		WordRemberApplication application = (WordRemberApplication)getApplication();
		mFragment.selectCurrentChapter( application.getBook() );
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.word_fragment, container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
		}
	}

}
