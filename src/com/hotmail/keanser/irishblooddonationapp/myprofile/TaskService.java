package com.hotmail.keanser.irishblooddonationapp.myprofile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import com.hotmail.keanser.irishblooddonationapp.CheckNetwork;
import com.hotmail.keanser.irishblooddonationapp.R;
import com.hotmail.keanser.irishblooddonationapp.bloodlevels.BloodLevels;

public class TaskService extends IntentService {

	private ArrayList<BloodLevels> BloodLevelList;

	private String bloodLevel;
	private String bloodType;
	private Integer bloodLevelDays;

	public TaskService() {
		super("TaskService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		// Pass through the blood type from AlarmReceiver
		Bundle bundle = intent.getExtras();
		bloodType = bundle.getString("currentBloodType2");

		// Only perform check if there is an internet connection
		if (CheckNetwork.isInternetAvailable(TaskService.this)) {

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

			String bloodLevelReplace = BloodLevelList.get(0).getBloodLevel()
					.replaceAll("Blood supply", "");

			bloodLevel = ("Current blood levels for "
					+ BloodLevelList.get(0).getBloodType() + " is" + bloodLevelReplace);

			// Get the number of days from the blood level string
			Scanner in = new Scanner(bloodLevel).useDelimiter("[^0-9]+");
			int bloodLevelDays = in.nextInt();

			// Send notification if no. of days blood level supply goes below 5
			if (bloodLevelDays < 5) {

				this.sendNotification(this);

			}

		} else {
			Toast.makeText(
					TaskService.this,
					"Unable to perform daily blood level check, no internet connection.",
					Toast.LENGTH_LONG).show();
		}

	}

	private void sendNotification(Context context) {

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Blood Level Notification")
				.setContentText(bloodLevel);

		Intent resultIntent = new Intent(this, MyProfileActivity.class);

		// Because clicking the notification opens a new ("special") activity,
		// there's no need to create an artificial back stack.
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
				resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(resultPendingIntent);

		// Sets an ID for the notification
		int mNotificationId = 001;
		// Gets an instance of the NotificationManager service
		NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(mNotificationId, mBuilder.build());

	}
}
