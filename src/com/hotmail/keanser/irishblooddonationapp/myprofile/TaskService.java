package com.hotmail.keanser.irishblooddonationapp.myprofile;

import java.util.Scanner;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.hotmail.keanser.irishblooddonationapp.R;

public class TaskService extends IntentService {

	private String bloodLevel;
	
	public TaskService() {
		super("TaskService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		// Do logic here and execute notification...
		Bundle bundle = intent.getExtras();
		bloodLevel = bundle.getString("currentBloodLevel2");

		Scanner in = new Scanner(bloodLevel).useDelimiter("[^0-9]+");
		int bloodLevelDays = in.nextInt();
		
		// Send notification if no. of days blood level supply goes below 5
		if (bloodLevelDays < 5) {
			this.sendNotification(this);
		}

	}

	private void sendNotification(Context context) {
		
		NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(this)
			    .setSmallIcon(R.drawable.ic_launcher)
			    .setContentTitle("Blood Level Notification")
			    .setContentText(bloodLevel);
		
		Intent resultIntent = new Intent(this, MyProfileActivity.class);

		// Because clicking the notification opens a new ("special") activity, there's
		// no need to create an artificial back stack.
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
				resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		mBuilder.setContentIntent(resultPendingIntent);
		
		// Sets an ID for the notification
		int mNotificationId = 001;
		// Gets an instance of the NotificationManager service
		NotificationManager mNotifyMgr = 
		        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(mNotificationId, mBuilder.build());
		
		
	}

}
