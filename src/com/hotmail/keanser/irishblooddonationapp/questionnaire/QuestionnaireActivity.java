package com.hotmail.keanser.irishblooddonationapp.questionnaire;

import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hotmail.keanser.irishblooddonationapp.R;

public class QuestionnaireActivity extends Activity {

	DatabaseHandler db = new DatabaseHandler(this);
	int counter;
	TextView tView;
	Button noButton;
	Button yesButton;
	Button backButton;
	Button homeButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questionnaire);
		// Show the Up button in the action bar.
		setupActionBar();

		// Only need to insert questions from sqllite the first time
		if (db.getAllQuestionnaires().isEmpty()) {
			/**
			 * CRUD Operations
			 * */
			// Inserting questions and answers
			Log.d("Insert: ", "Inserting ..");

			String[] questionArray = getResources().getStringArray(
					R.array.questions);
			String[] responseArray = getResources().getStringArray(
					R.array.responses);

			for (int i = 0; i < questionArray.length; i++) {
				db.addQuestionnaire(new Questionnaire(i, questionArray[i],
						responseArray[i]));
			}

		}

		// Set default text view to be the first question
		tView = (TextView) findViewById(R.id.questionview);
		tView.setText(db.getQuestionnaire(1).getQuestion());

		counter = 1;

		// Set the appearance behavior of each of the buttons
		noButton = (Button) findViewById(R.id.NoButton);
		noButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (counter == 10) {
					noButton.setVisibility(View.INVISIBLE);
					yesButton.setVisibility(View.INVISIBLE);
					tView.setText(R.string.success);
				} else {
					counter++;
					tView.setText(db.getQuestionnaire(counter).getQuestion());
					backButton.setVisibility(View.VISIBLE);
				}
			}
		});

		yesButton = (Button) findViewById(R.id.YesButton);
		yesButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if ((counter == 4) || (counter == 5) || (counter == 8)	|| (counter == 9)) {

					tView.setText(db.getQuestionnaire(counter).getResponse());

					noButton.setVisibility(View.INVISIBLE);
					backButton.setVisibility(View.VISIBLE);

					yesButton.setText("Add reminder");

					yesButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {

							Intent calIntent = new Intent(Intent.ACTION_EDIT);
							calIntent.setType("vnd.android.cursor.item/event");
							calIntent.putExtra(Events.TITLE,"Blood Donation Reminder");
							calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
							startActivity(calIntent);
						}
					});
				} else {
					tView.setText(db.getQuestionnaire(counter).getResponse());

					noButton.setVisibility(View.INVISIBLE);
					yesButton.setVisibility(View.INVISIBLE);
					backButton.setVisibility(View.VISIBLE);
				}

			}
		});

		backButton = (Button) findViewById(R.id.BackButton);
		backButton.setVisibility(View.INVISIBLE);
		backButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				String yesButtonText = yesButton.getText().toString();

				if (yesButtonText.equals("Add reminder")) {

					yesButton.setText("Yes");

					yesButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {

							if ((counter == 4) || (counter == 5)
									|| (counter == 8) || (counter == 9)) {

								tView.setText(db.getQuestionnaire(counter)
										.getResponse());

								noButton.setVisibility(View.INVISIBLE);
								backButton.setVisibility(View.VISIBLE);

								yesButton.setText("Add reminder");

								yesButton
										.setOnClickListener(new View.OnClickListener() {
											public void onClick(View v) {

												Intent calIntent = new Intent(Intent.ACTION_EDIT);
												calIntent.setType("vnd.android.cursor.item/event");
												calIntent.putExtra(Events.TITLE,"Blood Donation Reminder");
												calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
												startActivity(calIntent);
											}
										});
							} else {
								tView.setText(db.getQuestionnaire(counter).getResponse());

								noButton.setVisibility(View.INVISIBLE);
								yesButton.setVisibility(View.INVISIBLE);
								backButton.setVisibility(View.VISIBLE);
							}

						}
					});

				}

				if (noButton.isShown() && counter != 1) {
					counter--;
					tView.setText(db.getQuestionnaire(counter).getQuestion());

					// Set back button to invisible if going back from Q2 to Q1
					if (counter == 1) {
						backButton.setVisibility(View.INVISIBLE);
					}

				} else if (counter == 11) {
					counter--;
					noButton.setVisibility(View.VISIBLE);
					yesButton.setVisibility(View.VISIBLE);
					tView.setText(db.getQuestionnaire(counter).getQuestion());
				} else {
					noButton.setVisibility(View.VISIBLE);
					yesButton.setVisibility(View.VISIBLE);
					tView.setText(db.getQuestionnaire(counter).getQuestion());
				}

			}
		});

		homeButton = (Button) findViewById(R.id.HomeButton);
		homeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// finish questionnaire activity and return to home screen
				finish();
			}
		});

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.questionnaire, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
