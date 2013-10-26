/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package transapps.gpxfitness.ui;

import java.util.ArrayList;
import java.util.Calendar;

import transapps.gpxfitness.R;
import transapps.gpxfitness.db.HistoryAccessor;
import transapps.gpxfitness.db.ProfileAccessor;
import transapps.gpxfitness.piechart.PieChart;
import transapps.gpxfitness.piechart.PieChart.OnSelectedLisenter;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
	 * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will display the three primary sections of the app, one at a
	 * time.
	 */
	ViewPager mViewPager;
	ActionBar actionBar;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three primary sections
		// of the app.
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

		// Set up the action bar.
		actionBar = getActionBar();

		// Specify that the Home/Up button should not be enabled, since there is no hierarchical
		// parent.
		actionBar.setHomeButtonEnabled(false);

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When swiping between different app sections, select the corresponding tab.
				// We can also use ActionBar.Tab#select() to do this if we have a reference to the
				// Tab.
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by the adapter.
			// Also specify this Activity object, which implements the TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(
					actionBar.newTab()
					.setIcon(mAppSectionsPagerAdapter.getPageIconUnselected(i))
					.setTabListener(this));
		}	
		mViewPager.setCurrentItem(2); 
		if(!ProfileAccessor.isProfileSet())
		{
			profileAlertDialog();
		}
	//	actionBar.setTitle(this.getString(R.string.app_name) + ": " + ProfileAccessor.getUsername());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.edit_profile:
	            editProfileAlertDialog();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	public void profileAlertDialog()
	{
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.profile_dialog);
		dialog.setTitle("Please Enter Profile:");
		dialog.setCancelable(false);

		Button ok_button = (Button) dialog.findViewById(R.id.button);
		ok_button.setText("Ok");
		final EditText username = (EditText) dialog.findViewById(R.id.username);
		final EditText height_ft = (EditText) dialog.findViewById(R.id.height_ft);
		final EditText height_in = (EditText) dialog.findViewById(R.id.height_in);
		final EditText weight_lbs = (EditText) dialog.findViewById(R.id.weight_lbs);
		final EditText age_yrs = (EditText) dialog.findViewById(R.id.age_yrs);
		final RadioButton male = (RadioButton) dialog.findViewById(R.id.male);
		final EditText[] et = {username, height_ft, height_in, weight_lbs, age_yrs};
		ok_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String user_string = username.getText().toString();
				String height_ft_string = height_ft.getText().toString();
				String height_in_string = height_in.getText().toString();
				String weight_lbs_string = weight_lbs.getText().toString();
				String age_yrs_string = age_yrs.getText().toString();
				boolean are_any_empty = false;
				for(EditText e: et)
				{
					String str = e.getText().toString();
					if(str==null || str.length()==0)
						are_any_empty = true;
				}
				if(!are_any_empty)
				{
					int height = Integer.parseInt(height_ft_string)*12 + Integer.parseInt(height_in_string);
					String sex;
					if(male.isChecked())
						sex = "male";
					else
						sex = "female"; 
					ProfileAccessor.createNewProfile(user_string, height, Integer.parseInt(weight_lbs_string), sex, Integer.parseInt(age_yrs_string));
					dialog.dismiss();
				}
			}
		});

		dialog.show();
	}
	public void editProfileAlertDialog()
	{
		if(!ProfileAccessor.isProfileSet())
		{
			profileAlertDialog();
			return;
		}
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.profile_dialog);
		dialog.setTitle("Edit Profile:");
		dialog.setCancelable(true);

		Button ok_button = (Button) dialog.findViewById(R.id.button);
		ok_button.setText("Ok");
		final EditText username = (EditText) dialog.findViewById(R.id.username);
		username.setText(ProfileAccessor.getUsername());
		final EditText height_ft = (EditText) dialog.findViewById(R.id.height_ft);
		height_ft.setText("" + (int) ProfileAccessor.getHeight()/12);
		final EditText height_in = (EditText) dialog.findViewById(R.id.height_in);
		height_in.setText("" + (int) ProfileAccessor.getHeight()%12);
		final EditText weight_lbs = (EditText) dialog.findViewById(R.id.weight_lbs);
		weight_lbs.setText(ProfileAccessor.getWeight()+"");
		final EditText age_yrs = (EditText) dialog.findViewById(R.id.age_yrs);
		age_yrs.setText(ProfileAccessor.getAge()+"");
		final RadioButton male = (RadioButton) dialog.findViewById(R.id.male);
		final RadioButton female = (RadioButton) dialog.findViewById(R.id.female);
		if(ProfileAccessor.getSex().equals("male")){
			male.setChecked(true);
			female.setChecked(false);
		}
		else{
			male.setChecked(false);
			female.setChecked(true);
		}
		final EditText[] et = {username, height_ft, height_in, weight_lbs, age_yrs};
		ok_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String user_string = username.getText().toString();
				String height_ft_string = height_ft.getText().toString();
				String height_in_string = height_in.getText().toString();
				Log.d("TEXT", height_in_string);
				String weight_lbs_string = weight_lbs.getText().toString();
				String age_yrs_string = age_yrs.getText().toString();
				boolean are_any_empty = false;
				for(EditText e: et)
				{
					String str = e.getText().toString();
					if(str==null || str.length()==0)
						are_any_empty = true;
				}
				if(!are_any_empty)
				{
					int height = Integer.parseInt(height_ft_string)*12 + Integer.parseInt(height_in_string);
					String sex;
					if(male.isChecked())
						sex = "male";
					else
						sex = "female"; 
					//ProfileAccessor.createNewProfile(user_string, height, Integer.parseInt(weight_lbs_string), sex, Integer.parseInt(age_yrs_string));
					ProfileAccessor.changeUsername(user_string);
					//actionBar.setTitle(getString(R.string.app_name) + ": " + ProfileAccessor.getUsername());
					ProfileAccessor.changeHeight(height);
					ProfileAccessor.changeWeight(Double.parseDouble(weight_lbs_string));
					ProfileAccessor.changeSex(sex);
					ProfileAccessor.changeAge(Integer.parseInt(age_yrs_string));
					dialog.dismiss();
				}
			}
		});

		dialog.show();
	}
	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		tab.setIcon(mAppSectionsPagerAdapter.getPageIconUnselected(tab.getPosition()));
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
		tab.setIcon(mAppSectionsPagerAdapter.getPageIconSelected(tab.getPosition()));

	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
	 * sections of the app.
	 */
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				// The first section of the app is the most interesting -- it offers
				// a launchpad into the other demonstrations in this example application.
				return new ExerciseFragment();			
			case 2:
				return new HistoryFragment();
				// The other sections of the app are dummy placeholders.
			case 3:
				return new AnalyticsFragment();
			default:
				Fragment fragment = new DailySummaryFragment();
				Bundle args = new Bundle();
				args.putInt(DailySummaryFragment.ARG_SECTION_NUMBER, i + 1);
				fragment.setArguments(args);
				return fragment;

			}
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Section " + (position + 1);
		}
		public int getPageIconSelected(int position)
		{
			switch (position) {
			case 2:
				return R.drawable.ic_tab_user_green;
			case 0:
				return R.drawable.ic_tab_dumbell_green;
			case 1:
				return R.drawable.ic_tab_gpx_green;
			case 3:
				return R.drawable.ic_tab_history_green;
			case 4:
				return R.drawable.ic_tab_analytics_green;
			default:
				return R.drawable.ic_launcher;
			}
		}
		public int getPageIconUnselected(int position)
		{
			switch (position) {
			case 2:
				return R.drawable.ic_tab_user_gray;
			case 0:
				return R.drawable.ic_tab_dumbell_gray;
			case 1:
				return R.drawable.ic_tab_gpx_gray;
			case 3:
				return R.drawable.ic_tab_history_gray;
			case 4:
				return R.drawable.ic_tab_analytics_gray;
			default:
				return R.drawable.ic_launcher;
			}
		}

	}
}
