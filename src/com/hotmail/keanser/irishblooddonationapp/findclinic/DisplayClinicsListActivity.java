package com.hotmail.keanser.irishblooddonationapp.findclinic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hotmail.keanser.irishblooddonationapp.R;

public class DisplayClinicsListActivity extends Activity {

	TextView tv;
	private ListView listView;
	private ArrayList<Clinics> clinics;
	private String message;
	private String countySelected;
	String str = null;
	Integer pageNumber = 1;

	// Progress Dialog
	private ProgressDialog pDialog;

	// Progress dialog type (0 - for Horizontal progress bar)
	public static final int progress_bar_type = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_clinics_list);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			message = extras.getString("radioChosen");
			countySelected = extras.getString("radioCountySelected");

			DisplayClinicsListActivity.this.setTitle(countySelected);
			
			// Trying to avoid possible issue with asynctask below
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				new ClinicParse()
						.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			else
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

					Intent intent = new Intent(DisplayClinicsListActivity.this,
							DisplayClinicInfoActivity.class);
					intent.putExtra("listItemChosen", str);
					intent.putExtra("radioCountySelected", countySelected);
					startActivity(intent);

				}
			});

		}

	}

	private class ClinicParse extends
			AsyncTask<String, String, ArrayList<Clinics>> {

		@Override
		protected void onPreExecute() {

			showDialog(progress_bar_type);
			
		}

		protected ArrayList<Clinics> doInBackground(String... arg0) {

			clinics = new ArrayList<Clinics>();

			try {

				// HTTP PROTOCOL
				Document doc = Jsoup
						.connect(message)
						.timeout(0)
						.userAgent(
								"Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
						.cookie("auth", "token").get();

				Elements clinicpages1 = doc.select("div.Pages");

				// Check if more than one clinic information page exists
				if (!clinicpages1.isEmpty()) {

					// Get the number of pages and store it in a variable
					Elements clinicpages2 = clinicpages1.select("a[href]");

					// CREATE LIST AND POPULATE IT WITH ELEMENTS RETRIEVE FROM
					// HTML
					List<String> ClinicsMultipleList = new ArrayList<String>();

					ClinicsMultipleList.add(new String(message));

					for (Element clinicpages : clinicpages2) {
						ClinicsMultipleList.add(new String(message
								+ clinicpages.attr("href")));
					}

					for (int i = 0; i < ClinicsMultipleList.size(); i++) {

						publishProgress(""
								+ (int) ((i / (float) ClinicsMultipleList
										.size()) * 100));

						// HTTP PROTOCOL
						Document multipledoc = Jsoup
								.connect(ClinicsMultipleList.get(i))
								.timeout(0)
								.userAgent(
										"Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
								.cookie("auth", "token").get();

						// EXTRACT SPECIFIC ELEMENTS FROM THE HTML
						Elements cliniclist1 = multipledoc.select("ul#mainDoclist");
						Elements cliniclist2 = cliniclist1.select("a[href]");

						// CREATE LIST AND POPULATE IT WITH ELEMENTS RETRIEVE
						// FROM
						// HTML
						List<String> ClinicsRawList = new ArrayList<String>();

						for (Element cliniclist : cliniclist2) {

							ClinicsRawList.add(new String(cliniclist.text()));
							ClinicsRawList.add(new String(
									"http://www.giveblood.ie"
											+ cliniclist.attr("href")));

						}

						for (int x = 0; x < ClinicsRawList.size(); x += 2) {
							clinics.add(new Clinics(ClinicsRawList.get(x),
									ClinicsRawList.get(x + 1)));
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

						publishProgress("" + (int) ((i / (float) ClinicsRawList.size()) * 100));

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

		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
			pDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(ArrayList<Clinics> Clinicslist) {

			if (!Clinicslist.isEmpty()) {

				// Sort clinics by name
				Collections.sort(Clinicslist, new Comparator<Clinics>() {
					@Override
					public int compare(Clinics data1, Clinics data2) {
						return data1.getClinicDetails().compareToIgnoreCase(
								data2.getClinicDetails());
					}
				});

				// Set the adapter to the listview
				listView.setAdapter(new ClinicsAdapter(
						DisplayClinicsListActivity.this, Clinicslist));
			} else {
				Toast.makeText(getBaseContext(), "No clinic information found",
						Toast.LENGTH_LONG).show();
			}

			// dismiss the dialog after the clinic list loaded
			removeDialog(progress_bar_type);
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		
		switch (id) {
		case progress_bar_type:
			
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("Retrieving list of clinics. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setMax(100);
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setCancelable(true);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.show();
			return pDialog;
		default:
			return null;
		}
	}

}
