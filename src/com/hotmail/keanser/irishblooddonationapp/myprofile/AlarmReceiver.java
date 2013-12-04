package com.hotmail.keanser.irishblooddonationapp.myprofile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	
	public static final String LOG_TAG = "BootReceiver";
	public static String ACTION_ALARM = "com.alarammanager.alaram";

   // onReceive must be very quick and not block, so it just fires up a Service
   @Override
   public void onReceive(Context context, Intent intent) {
      Log.i(LOG_TAG, "AlarmReceiver invoked, starting Alarm Service in background");
      
		Bundle bundle = intent.getExtras();
		String bloodLevel = bundle.getString("currentBloodLevel");

		Intent serviceIntent = new Intent(context, TaskService.class);
		serviceIntent.putExtra("currentBloodLevel2", bloodLevel);

		context.startService(serviceIntent);
   }
}