//package com.hotmail.keanser.irishblooddonationapp.myprofile;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.SystemClock;
//import android.util.Log;
//
//public class BootReceiver extends BroadcastReceiver {
//
//	public static final String LOG_TAG = "BootReceiver";
//	public static final long ALARM_INTERVAL = 10000;
//	public static final long ALARM_TRIGGER_AT_TIME = SystemClock.elapsedRealtime() + 30000;
//
//	@Override
//	public void onReceive(Context context, Intent intent) {
//		Log.i(LOG_TAG, "DealBootReceiver invoked, configuring AlarmManager");
//		
//		AlarmManager alarmMgr = (AlarmManager) context
//				.getSystemService(Context.ALARM_SERVICE);
//		
//		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
//				new Intent(context, AlarmReceiver.class), 0);
//
//		// use inexact repeating which is easier on battery (system can phase
//		// events and not wake at exact times)
//		alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//				ALARM_TRIGGER_AT_TIME, ALARM_INTERVAL,
//				pendingIntent);
//		
//	}
//}