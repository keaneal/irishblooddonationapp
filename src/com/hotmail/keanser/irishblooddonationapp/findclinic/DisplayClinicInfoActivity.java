package com.hotmail.keanser.irishblooddonationapp.findclinic;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hotmail.keanser.irishblooddonationapp.CheckNetwork;
import com.hotmail.keanser.irishblooddonationapp.R;

public class DisplayClinicInfoActivity extends Activity implements
		OnClickListener {

	private String message;
	private String countySelected;
	private String clinicInfoHeader;
	private ArrayList<String> clinicInfo;
	Date date;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_clinic_info);

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			message = extras.getString("listItemChosen");
			countySelected = extras.getString("radioCountySelected");

			new ClinicInfoHeaderParse().execute();

			new ClinicInfoParse().execute();

			Button buttonAddCalendar = (Button) findViewById(R.id.buttonAddCalendar);
			buttonAddCalendar.setOnClickListener(this);

			Button buttonFindMaps = (Button) findViewById(R.id.buttonFindMaps);
			buttonFindMaps.setOnClickListener(this);

			Button buttonOpenInfoPage = (Button) findViewById(R.id.buttonOpenInfoPage);
			buttonOpenInfoPage.setOnClickListener(this);

		}

	}

	class ClinicInfoHeaderParse extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... arg0) {

			try {

				// HTTP PROTOCOL
				Document doc = Jsoup
						.connect(message)
						.timeout(5000)
						.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
						.cookie("auth", "token").get();

				// Extract the header
				Elements clinicInfoHeader2 = doc.select("div#largecentre");
				Elements clinicInfoHeader1 = clinicInfoHeader2
						.select("h1.heading");

				clinicInfoHeader = new String(clinicInfoHeader1.first().text());

			} catch (IOException ioex) {
				ioex.printStackTrace();
			}

			return clinicInfoHeader;

		}

		@Override
		protected void onPostExecute(String cih) {

			DisplayClinicInfoActivity.this.setTitle(cih);
		}

	}

	class ClinicInfoParse extends AsyncTask<String, Void, ArrayList<String>> {

		String clinicinfotext = "";

		// CAST THE LINEARLAYOUT HOLDING THE MAIN PROGRESS (SPINNER)
		LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);

		@Override
		protected void onPreExecute() {
			// SHOW THE SPINNER WHILE LOADING FEEDS
			linlaHeaderProgress.setVisibility(View.VISIBLE);
		}

		@Override
		protected ArrayList<String> doInBackground(String... arg0) {

			clinicInfo = new ArrayList<String>();

			clinicInfoHeader = new String();

			try {

				// HTTP PROTOCOL
				Document doc = Jsoup
						.connect(message)
						.timeout(5000)
						.userAgent(
								"Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
						.cookie("auth", "token").get();

				// EXTRACT SPECIFIC ELEMENTS FROM THE HTML
				Elements clinicinfo1 = doc.select("div.ClinicsLayout");

				clinicinfotext = clinicinfo1.text();

				// String pattern to parse the name of the venue, date and time
				String namePattern = "(?<=Venue: )(.*)(?= Date:)";
				String datePattern = "(?<=Date: )(.*)(?= Times:)";
				String timePattern = "(?<=Times: )(.*)";

				// Create a Pattern object
				Pattern np = Pattern.compile(namePattern);
				Pattern dp = Pattern.compile(datePattern);
				Pattern tp = Pattern.compile(timePattern);

				// Apply the pattern to the text
				Matcher mnp = np.matcher(clinicinfotext);
				Matcher mdp = dp.matcher(clinicinfotext);
				Matcher mtp = tp.matcher(clinicinfotext);

				// add any found string to an array
				if (mnp.find()) {
					clinicInfo.add(mnp.group(0));
				} else {
					clinicInfo.add("");
				}

				if (mdp.find()) {
					clinicInfo.add(mdp.group(0));
				} else {
					clinicInfo.add("");
				}

				if (mtp.find()) {
					clinicInfo.add(mtp.group(0));
				} else {
					clinicInfo.add("");
				}

			} catch (IOException ioex) {
				ioex.printStackTrace();
			}

			return clinicInfo;

		}

		@Override
		protected void onPostExecute(ArrayList<String> ci) {

			// Get and display clinic location
			TextView textViewVenue = (TextView) findViewById(R.id.textViewVenue);
			if (clinicInfo.get(0).isEmpty()) {
				String sourceString = "<b>" + "Venue: " + "</b> "
						+ "No venue name available";
				textViewVenue.setText(Html.fromHtml(sourceString));
			} else {
				String sourceString = "<b>" + "Venue: " + "</b> "
						+ clinicInfo.get(0);
				textViewVenue.setText(Html.fromHtml(sourceString));
			}

			// Get and display clinic date
			TextView textViewDate = (TextView) findViewById(R.id.textViewDate);
			if (clinicInfo.get(1).isEmpty()) {
				String sourceString = "<b>" + "Date: " + "</b> "
						+ "No venue date available";
				textViewDate.setText(Html.fromHtml(sourceString));
			} else {
				String sourceString = "<b>" + "Date: " + "</b> "
						+ clinicInfo.get(1);
				textViewDate.setText(Html.fromHtml(sourceString));
			}

			// Get and display clinic time
			TextView textViewTime = (TextView) findViewById(R.id.textViewTime);
			if (clinicInfo.get(2).isEmpty()) {
				String sourceString = "<b>" + "Time: " + "</b> "
						+ "No venue time available";
				textViewTime.setText(Html.fromHtml(sourceString));
			} else {
				String sourceString = "<b>" + "Time: " + "</b> "
						+ clinicInfo.get(2);
				textViewTime.setText(Html.fromHtml(sourceString));
			}

			// Parse and format string date to date
			String inputDate = clinicInfo.get(1);
			String formattedDate = dateLongStringConvert(inputDate);

			try {
				date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
						.parse(formattedDate); // parse input date
			} catch (ParseException pex) {
				pex.printStackTrace();
			}

			// HIDE THE SPINNER AFTER LOADING FEEDS
			linlaHeaderProgress.setVisibility(View.GONE);

		}

	}

	// Implement the OnClickListener callback
	public void onClick(View v) {

		// Remove unnecessary text from venue name to aid search
		String parsedVenueName = clinicInfo.get(0)
				.replaceAll(", Please Note Days & Clinic Opening Times", "")
				.replaceAll(", Please Note Day & Clinic Opening Times", "")
				.replaceAll("Please Note Venue & Clinic Opening Times", "")
				.replaceAll("Please Note Day & Clinic Opening Times", "")
				.replaceAll("Please Note Days & Clinic Opening Times", "")
				.replaceAll(" Please Note Clinic Opening Times", "")
				.replaceAll(" Please Note Clinic Opening Time", "")
				.replaceAll("PLEASE NOTE REVISED TIMES: ", "");

		// Remove City and North from county name to aid search
		String parsedCountyName = countySelected.replaceAll("City", "")
				.replaceAll("North", "");

		switch (v.getId()) {

		case R.id.buttonAddCalendar:
			if (!clinicInfo.get(1).isEmpty()) {

				Intent calIntent = new Intent(Intent.ACTION_EDIT);
				calIntent.setType("vnd.android.cursor.item/event");
				calIntent.putExtra(Events.TITLE, "Blood Donation Reminder");
				calIntent.putExtra(Events.EVENT_LOCATION, parsedVenueName
						+ ", " + parsedCountyName);
				calIntent.putExtra(Events.DESCRIPTION, clinicInfo.get(2));
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
				calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
						c.getTimeInMillis());
				calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
						c.getTimeInMillis());
				startActivity(calIntent);
			} else {
				Toast.makeText(getBaseContext(), "No clinic date found?",
						Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.buttonFindMaps:

			if (!clinicInfo.get(0).isEmpty()) {

				double latitude = 0;
				double longitude = 0;

				String geoUriString = "geo:" + latitude + "," + longitude
						+ "?q=" + parsedVenueName + ", " + parsedCountyName
						+ ", Ireland";

				// String geoUriString = clinicinfo.get(0);
				Uri geoUri = Uri.parse(geoUriString);
				Intent mapCall = new Intent(Intent.ACTION_VIEW, geoUri);
				startActivity(mapCall);
			} else {
				Toast.makeText(getBaseContext(), "No clinic location found?",
						Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.buttonOpenInfoPage:
			if (CheckNetwork
					.isInternetAvailable(DisplayClinicInfoActivity.this)) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(message));
				startActivity(browserIntent);
			} else {
				Toast.makeText(DisplayClinicInfoActivity.this,
						"No Internet Connection", Toast.LENGTH_LONG).show();
			}
			break;
		}
	}

	private String dateLongStringConvert(String dateLongString) {

		if (dateLongString.isEmpty()) {
			return "No information available";
		} else {

			// split long date string into string array
			String[] dateArray = dateLongString.split("\\s+");

			// get day of month as an integer (strip out non numeric chars)
			int dayOfMonth = Integer.parseInt(dateArray[0].replaceAll("\\D+",
					""));

			// Convert month string to number
			String month = "";
			if (dateArray[1].equals("January")) {
				month = "01";
			} else if (dateArray[1].equals("February")) {
				month = "02";
			} else if (dateArray[1].equals("March")) {
				month = "03";
			} else if (dateArray[1].equals("April")) {
				month = "04";
			} else if (dateArray[1].equals("May")) {
				month = "05";
			} else if (dateArray[1].equals("June")) {
				month = "06";
			} else if (dateArray[1].equals("July")) {
				month = "07";
			} else if (dateArray[1].equals("August")) {
				month = "08";
			} else if (dateArray[1].equals("September")) {
				month = "09";
			} else if (dateArray[1].equals("October")) {
				month = "10";
			} else if (dateArray[1].equals("November")) {
				month = "11";
			} else if (dateArray[1].equals("December")) {
				month = "12";
			}

			// return formated date string
			return String.format("%02d", dayOfMonth) + "-" + month + "-"
					+ dateArray[2];
		}
	}

}
