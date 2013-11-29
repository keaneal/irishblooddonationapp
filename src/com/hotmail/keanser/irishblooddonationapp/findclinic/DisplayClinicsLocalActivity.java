package com.hotmail.keanser.irishblooddonationapp.findclinic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hotmail.keanser.irishblooddonationapp.R;

public class DisplayClinicsLocalActivity extends Activity {

	TextView tv;
	private ListView listView;
	private ArrayList<Clinics> clinics;
	private String message;
	String str = null;
	Integer pageNumber = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clinics_list);

		// Get radio button selected from previous activity
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
			message = extras.getString("radioChosen");

			new ClinicParse().execute();

			// Set up main activity as a list
			listView = (ListView) findViewById(R.id.ListView02);

			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					// String str = ((TextView)view).getText().toString();

					LinearLayout ll = (LinearLayout) view;
					TextView tv = (TextView) ll.findViewById(R.id.clinicLink);
					final String str = tv.getText().toString();

//					Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG)
//							.show();

					Intent intent = new
					Intent(DisplayClinicsLocalActivity.this, DisplayClinicsLocalActivity.class);
					intent.putExtra("radioChosen", str); // pass "str" to the next Activity
					startActivity(intent);

				}
			});

		}

	}

	private class ClinicParse extends
			AsyncTask<String, Void, ArrayList<Clinics>> {

		// CAST THE LINEARLAYOUT HOLDING THE MAIN PROGRESS (SPINNER)
		LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);

		@Override
		protected void onPreExecute() {
			// SHOW THE SPINNER WHILE LOADING FEEDS
			linlaHeaderProgress.setVisibility(View.VISIBLE);
		}

		protected ArrayList<Clinics> doInBackground(String... arg0) {
			
			clinics = new ArrayList<Clinics>();

			try {

				// HTTP PROTOCOL
				Document doc = Jsoup
						.connect(message)
						.timeout(5000)
						.userAgent(
								"Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
						.cookie("auth", "token").get();

				Elements clinicpages1 = doc.select("div.Pages");

				// Check if multiple pages exist and parse them accordingly
				if (!clinicpages1.isEmpty()) {

					// Get the number of pages and store it in a variable
					Elements clinicpages2 = clinicpages1.select("a[href]");

					// CREATE LIST AND POPULATE IT WITH ELEMENTS RETRIEVE FROM
					// HTML
					List<String> ClinicsMultipleList = new ArrayList<String>();
					
					ClinicsMultipleList.add(new String(message));

					for (Element clinicpages : clinicpages2) {
						ClinicsMultipleList.add(new String(message + clinicpages.attr("href")));
					}

					for (int i = 0; i < ClinicsMultipleList.size(); i++) {
						// HTTP PROTOCOL
						Document multipledoc = Jsoup
								.connect(ClinicsMultipleList.get(i))
								.timeout(5000)
								.userAgent(
										"Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
								.cookie("auth", "token").get();
						
						// EXTRACT SPECIFIC ELEMENTS FROM THE HTML
						Elements cliniclist1 = multipledoc.select("ul#mainDoclist");
						Elements cliniclist2 = cliniclist1.select("a[href]");

						// CREATE LIST AND POPULATE IT WITH ELEMENTS RETRIEVE FROM
						// HTML
						List<String> ClinicsRawList = new ArrayList<String>();

						for (Element cliniclist : cliniclist2) {

							ClinicsRawList.add(new String(cliniclist.text()));
							ClinicsRawList.add(new String("http://www.giveblood.ie"
									+ cliniclist.attr("href")));

						}

						for (int x = 0; x < ClinicsRawList.size(); x += 2) {
							clinics.add(new Clinics(ClinicsRawList.get(x), ClinicsRawList.get(x + 1)));
						}

					}

				} else {

					// EXTRACT SPECIFIC ELEMENTS FROM THE HTML
					Elements cliniclist1 = doc.select("ul#mainDoclist");
					Elements cliniclist2 = cliniclist1.select("a[href]");

					// CREATE LIST AND POPULATE IT WITH ELEMENTS RETRIEVE FROM
					// HTML
					List<String> ClinicsRawList = new ArrayList<String>();

					for (Element cliniclist : cliniclist2) {

						ClinicsRawList.add(new String(cliniclist.text()));
						ClinicsRawList.add(new String("http://www.giveblood.ie"
								+ cliniclist.attr("href")));

					}

					for (int i = 0; i < ClinicsRawList.size(); i += 2) {
						clinics.add(new Clinics(ClinicsRawList.get(i),
								ClinicsRawList.get(i + 1)));
					}
				}

			}

			catch (IOException ioex) {
				ioex.printStackTrace();
			}

			return clinics;

		}

		@Override
		protected void onPostExecute(ArrayList<Clinics> Clinicslist) {
			// SET THE ADAPTER TO THE LISTVIEW
			listView.setAdapter(new ClinicsAdapter(
					DisplayClinicsLocalActivity.this, Clinicslist));

			// HIDE THE SPINNER AFTER LOADING FEEDS
			linlaHeaderProgress.setVisibility(View.GONE);
		}

	}

}
