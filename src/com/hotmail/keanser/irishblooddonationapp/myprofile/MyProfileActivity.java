package com.hotmail.keanser.irishblooddonationapp.myprofile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hotmail.keanser.irishblooddonationapp.R;
import com.hotmail.keanser.irishblooddonationapp.bloodlevels.BloodLevels;
//import com.hotmail.keanser.irishblooddonationapp.myprofile.NDSpinner;

public class MyProfileActivity extends Activity implements
		OnItemSelectedListener {

	private ArrayList<BloodLevels> BloodLevelList;

	private NDSpinner spinnerBloodType;
	private TextView tvLastDate;
	private TextView tvNextDate;
	private TextView tvNextDateWarning;
	private TextView tvCurrentBloodLevel;
	private TextView tvLastRefreshed;
	private LinearLayout llAddReminder;
	private Button btnChangeDate;
	private Button btnAddReminder;
	private Button btnRefresh;
	private Calendar eligibleDate;
	private Calendar selectedDate;
	private Calendar todaysDate;
	private String bloodType;
	private String currentBloodLevel;
	private String currentBloodType;
	private String lastRefreshed;
	
	private ArrayList<String> bloodTypes;

	private int year;
	private int month;
	private int day;
	private int check=0;

	static final int DATE_DIALOG_ID = 999;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_myprofile);
		// Show the Up button in the action bar.
		setupActionBar();	
		
		spinnerBloodType = (NDSpinner) findViewById(R.id.spinnerbloodtype);
		tvCurrentBloodLevel = (TextView) findViewById(R.id.tvCurrentBloodLevel);
		tvLastRefreshed = (TextView) findViewById(R.id.tvLastRefreshed);
		
		// Convert string array array list of strings 
		String[] array = getResources().getStringArray(R.array.blood_types);
	    List<String> list = new ArrayList<String>();
	    list = Arrays.asList(array);
	    bloodTypes = new ArrayList<String>(list);
		
		// Get previously selected blood type
		SharedPreferences currentBloodLevelPref = getApplicationContext().getSharedPreferences("BloodLevelPrefName",
				Context.MODE_PRIVATE);
		currentBloodLevel = currentBloodLevelPref.getString("BloodLevelPrefName", "");
		
		// Get previously selected blood level
		SharedPreferences currentBloodTypePref = getApplicationContext().getSharedPreferences("bloodTypePref",
				Context.MODE_PRIVATE);
		currentBloodType = currentBloodTypePref.getString("bloodTypePref", "");
		
		// Get last refreshed refreshed date
		SharedPreferences lastRefreshedDatePref = getApplicationContext().getSharedPreferences("RefreshedDatePref",
				Context.MODE_PRIVATE);
		lastRefreshed = lastRefreshedDatePref.getString("RefreshedDatePref", "");
		
		// Very first time app is run currentBloodLevel and currentBloodType will be empty
		if ((!(currentBloodLevel.isEmpty())) || (!(currentBloodType.isEmpty())) 
				|| (!(lastRefreshed.isEmpty())) ) {

			// Set the previously selected blood type
			spinnerBloodType.setSelection(bloodTypes.indexOf(currentBloodType));
			
			// Set the previously selected blood level
			tvCurrentBloodLevel.setText(currentBloodLevel);
			
			// Set the last refreshed date
			tvLastRefreshed.setText(lastRefreshed);			
			
		} else {
			// Set the last refreshed text if blood type hasn't been selected yet
			tvLastRefreshed.setText(new StringBuilder()
					.append("Last refreshed: Never"));
		}
		
		addItemsOnSpinnerBloodType();
		setCurrentDateOnView();
		addListenerOnButton();
		
		// set selected date into last date textview
		tvLastDate.setText(new StringBuilder()
				.append("Date of last donation: No date set yet"));

		// set selected date into next date textview
		tvNextDate.setText(new StringBuilder()
				.append("Next eligible for donation: No date set yet"));

	}

	public void addItemsOnSpinnerBloodType() {
		spinnerBloodType.setOnItemSelectedListener(this);
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		
		check = check + 1;

		// When screen is opened initially wait for user to make 
		// selection before retrieving blood level
		if (check > 1) {
			bloodType = (String) parent.getItemAtPosition(pos);

			new BloodLevelParse().execute(bloodType);
			
			// We need an Editor object to make preference changes.
			SharedPreferences currentBloodTypePref = getApplicationContext().getSharedPreferences("bloodTypePref",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor1 = currentBloodTypePref.edit();
			editor1.putString("bloodTypePref", bloodType);

			// Commit the edits!
			editor1.commit();

			String stringDate = "Last refreshed: " + day + "-" + (month + 1) + "-" + year + " ";
						
			// set current date into textview
			tvLastRefreshed.setText(stringDate);	
			
			// We need an Editor object to make preference changes.
			SharedPreferences lastRefreshedDatePref = getApplicationContext().getSharedPreferences("RefreshedDatePref",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor2 = lastRefreshedDatePref.edit();
			editor2.putString("RefreshedDatePref", stringDate);
			
			// Commit the edits!
			editor2.commit();
			
		}		
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
	}

	// display current date
	public void setCurrentDateOnView() {

		tvLastDate = (TextView) findViewById(R.id.tvLastDate);
		tvNextDate = (TextView) findViewById(R.id.tvNextDate);
		tvNextDateWarning = (TextView) findViewById(R.id.tvNextDateWarning);
		llAddReminder = (LinearLayout) findViewById(R.id.llAddReminder);

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		// set current date into textview
		tvLastDate.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(month + 1).append("-").append(day).append("-")
				.append(year).append(" "));

	}

	public void addListenerOnButton() {

		btnChangeDate = (Button) findViewById(R.id.btnChangeDate);
		btnAddReminder = (Button) findViewById(R.id.btnAddReminder);
		btnRefresh = (Button) findViewById(R.id.btnRefresh);

		btnChangeDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
		
		btnAddReminder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
	
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");    
				
			    try {
					Date dateWithoutTime = sdf.parse(sdf.format(selectedDate.getTime()));
					
					Intent calIntent = new Intent(Intent.ACTION_EDIT);
					calIntent.setType("vnd.android.cursor.item/event");
					calIntent.putExtra(Events.TITLE, "Eligible to donate blood from today");
					calIntent.putExtra(Events.DESCRIPTION, "Last donated on: " + dateWithoutTime);
					calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
					calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
							eligibleDate.getTimeInMillis());
					calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
							eligibleDate.getTimeInMillis());
					startActivity(calIntent);
					
				} catch (ParseException e) {

					e.printStackTrace();
				}
			    
			}
		});
		
		btnRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				// Get previously selected blood level
				SharedPreferences currentBloodTypePref = getApplicationContext().getSharedPreferences("bloodTypePref",
						Context.MODE_PRIVATE);
				currentBloodType = currentBloodTypePref.getString("bloodTypePref", "");
				
				int item_postion= bloodTypes.indexOf(currentBloodType);// item which you want to click
				spinnerBloodType.setSelection(item_postion, true);
				View item_view = (View)spinnerBloodType.getChildAt(item_postion);
				long item_id = spinnerBloodType.getAdapter().getItemId(item_postion);
				spinnerBloodType.performItemClick(item_view, 0, item_id);
				
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// Get date selected 
			selectedDate = GregorianCalendar.getInstance();
			selectedDate.set(year, month, day);
			
			// Get date selected and add 90 days to it
			eligibleDate = GregorianCalendar.getInstance();
			eligibleDate.set(year, month, day);
			eligibleDate.add(Calendar.DATE, 90);
			

			// set selected date into textview
			tvLastDate.setText(new StringBuilder()
					.append("Date of last donation: ").append(day).append("-")
					.append(month + 1).append("-").append(year).append(" "));

			// set selected date into textview
			tvNextDate.setText(new StringBuilder()
					.append("Eligible for donation from (90 Days): ")
					.append(eligibleDate.get(Calendar.DAY_OF_MONTH)).append("-")
					.append(eligibleDate.get(Calendar.MONTH)).append("-")
					.append(eligibleDate.get(Calendar.YEAR)).append(" "));
			
			// set warning text for eligible date
			String sourceString = "<b>" + "Note: " + "</b> "
					+ "The eligible from date above assumes that you have "
					+ "no restrictions that may prevent you donating. To find out more about"
					+ " possible restrictions complete the questionnaire and/or visit Giveblood.ie";
			tvNextDateWarning.setText(Html.fromHtml(sourceString));
					
			// Get todays date
			todaysDate = GregorianCalendar.getInstance();

			// make the add reminder view visible if date is in the future
			if (eligibleDate.getTime().after(todaysDate.getTime())) {
				llAddReminder.setVisibility(View.VISIBLE);
			} else {
				llAddReminder.setVisibility(View.INVISIBLE);
			}
//			eligibleDate.setTime(c1.getTime());
		
		}
	};

	private class BloodLevelParse extends
			AsyncTask<String, Void, ArrayList<BloodLevels>> {

		// CAST THE LINEARLAYOUT HOLDING THE MAIN PROGRESS (SPINNER)
		LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);

		@Override
		protected void onPreExecute() {
			// SHOW THE SPINNER WHILE LOADING FEEDS
			linlaHeaderProgress.setVisibility(View.VISIBLE);
		}

		protected ArrayList<BloodLevels> doInBackground(String... params) {

			String bloodType = params[0];

			try {

				// HTTP PROTOCOL
				Document doc = Jsoup
						.connect(
								"http://www.giveblood.ie/Current_Blood_Supply/")
						.timeout(5000)
						.userAgent(
								"Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
						.cookie("auth", "token").get();

				// EXTRACT SPECIFIC ELEMENTS FROM THE HTML
				Elements bloodsupplylines1 = doc.select("td.bloodsupplyline");
				Elements bloodsupplylines2 = bloodsupplylines1
						.select("img[src~=(?i)\\.(png|jpe?g|gif)]");

				// CREATE LIST AND POPULATE IT WITH ELEMENTS RETRIEVE FROM HTML
				List<String> BloodLevelRawList = new ArrayList<String>();

				for (Element bloodsupplyline : bloodsupplylines2) {

					BloodLevelRawList.add(new String(bloodsupplyline
							.attr("alt")));

				}

				BloodLevelList = new ArrayList<BloodLevels>();

				for (int i = 0; i < BloodLevelRawList.size(); i += 2) {
					// Only add to BloodLevelList if it is the blood type
					// selected
					if (BloodLevelRawList.get(i).equals(bloodType)) {

						BloodLevelList.add(new BloodLevels(BloodLevelRawList
								.get(i), BloodLevelRawList.get(i + 1)));
					}
				}

			}

			catch (IOException ioex) {
				ioex.printStackTrace();
			}

			return BloodLevelList;

		}

		@Override
		protected void onPostExecute(ArrayList<BloodLevels> BloodLevelList) {

			// HIDE THE SPINNER AFTER LOADING FEEDS
			linlaHeaderProgress.setVisibility(View.GONE);

			

			String bloodLevel = BloodLevelList.get(0).getBloodLevel()
					.replaceAll("Blood supply", "");

			
			currentBloodLevel = ("Current blood levels for "
					+ BloodLevelList.get(0).getBloodType() + " is"
					+ bloodLevel);
			
			tvCurrentBloodLevel.setText(currentBloodLevel);
			
			
			// We need an Editor object to make preference changes.
			// All objects are from android.context.Context
			SharedPreferences currentBloodLevelPref = getApplicationContext().getSharedPreferences("BloodLevelPrefName",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = currentBloodLevelPref.edit();
			editor.putString("BloodLevelPrefName", currentBloodLevel);

			// Commit the edits!
			editor.commit();

		}

	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

}
