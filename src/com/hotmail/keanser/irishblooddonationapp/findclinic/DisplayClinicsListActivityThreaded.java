//package com.hotmail.keanser.irishblooddonationapp.findclinic;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.SystemClock;
//import android.util.Log;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.hotmail.keanser.irishblooddonationapp.R;
//
//public class DisplayClinicsListActivityThreaded extends Activity {
//
//	TextView tv;
//	private ListView listView;
//	private ArrayList<Clinics> clinics;
//	private String message;
//	String str = null;
//	Integer pageNumber = 1;
//
//	private Thread worker;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_display_clinics_list);
//
//		// Set up main activity as a list
//		listView = (ListView) findViewById(R.id.ListView02);
//		
//		// CAST THE LINEARLAYOUT HOLDING THE MAIN PROGRESS (SPINNER)
//		final LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
//		
//		// Get and make sure radio button selected is not null
//		Bundle extras = getIntent().getExtras();
//		if (extras != null) {
//			
//			message = extras.getString("radioChosen");
//
//			// Display the loading spinner before starting the worker thread
//			linlaHeaderProgress.setVisibility(View.VISIBLE);
//			
//			worker = new Thread(new Runnable() {
//
//				private void updateUI(final ArrayList<Clinics> clinics) {
//					if (worker.isInterrupted()) {
//						return;
//					}
//					runOnUiThread(new Runnable() {
//
//						@Override
//						public void run() {
//							// Update view and remove loading spinner etc...
//							
//							if (!clinics.isEmpty()) {
//								// SET THE ADAPTER TO THE LISTVIEW
//								listView.setAdapter(new ClinicsAdapter(
//										DisplayClinicsListActivityThreaded.this, clinics));
//							} else {
//								Toast.makeText(getBaseContext(), "No clinic information found", Toast.LENGTH_LONG)
//								.show();
//							}
//							
//							// HIDE THE SPINNER AFTER LOADING FEEDS
//							linlaHeaderProgress.setVisibility(View.GONE);
//	
//						}
//					});
//				}
//
//				private ArrayList<Clinics> download() {
//					
//					clinics = new ArrayList<Clinics>();
//					
//					try {
//					
//					// HTTP PROTOCOL
//					Document doc = Jsoup
//							.connect(message)
//							.timeout(10000)
//							.userAgent(
//									"Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//							.cookie("auth", "token").get();
//
//					// EXTRACT SPECIFIC ELEMENTS FROM THE HTML
//					Elements cliniclist1 = doc.select("ul#mainDoclist");
//					Elements cliniclist2 = cliniclist1.select("a[href]");
//
//					// CREATE LIST AND POPULATE IT WITH ELEMENTS RETRIEVE FROM
//					// HTML
//					List<String> ClinicsRawList = new ArrayList<String>();
//
//					for (Element cliniclist : cliniclist2) {
//
//						ClinicsRawList.add(new String(cliniclist.text()));
//						ClinicsRawList.add(new String("http://www.giveblood.ie"
//								+ cliniclist.attr("href")));
//
//					}
//
//					for (int i = 0; i < ClinicsRawList.size(); i += 2) {
//						clinics.add(new Clinics(ClinicsRawList.get(i),
//								ClinicsRawList.get(i + 1)));
//					}
//					
//					
//					}
//
//					catch (IOException ioex) {
//						ioex.printStackTrace();
//					}
//
//					return clinics;
//
//				}
//
//				@Override
//				public void run() {
//					Log.d("Alan", "Thread run()");
//					updateUI(download());
//				}
//
//			});
//			worker.start();
//		}
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		worker.interrupt();
//	}
//
//}
//
//
//// listView.setOnItemClickListener(new OnItemClickListener() {
//// @Override
//// public void onItemClick(AdapterView<?> parent, View view,
//// int position, long id) {
////
//// // String str = ((TextView)view).getText().toString();
////
//// LinearLayout ll = (LinearLayout) view;
//// TextView tv = (TextView) ll.findViewById(R.id.clinicLink);
//// final String str = tv.getText().toString();
////
//// Intent intent = new
//// Intent(DisplayClinicsListActivity.this, DisplayClinicInfoActivity.class);
//// intent.putExtra("listItemChosen", str); // pass "str" to the next
//// Activity
//// startActivity(intent);
////
//// }
//// });
//
