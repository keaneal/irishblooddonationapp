package com.hotmail.keanser.irishblooddonationapp;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.hotmail.keanser.irishblooddonationapp.bloodlevels.BloodLevelsActivity;
import com.hotmail.keanser.irishblooddonationapp.findclinic.DisplayClinicsRadioActivity;
import com.hotmail.keanser.irishblooddonationapp.myprofile.MyProfileActivity;
import com.hotmail.keanser.irishblooddonationapp.questionnaire.QuestionnaireActivity;

public class MainActivity extends Activity implements OnClickListener {

	public final static String EXTRA_MESSAGE = "com.hotmail.keanser.irishblooddonationapp.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button myProfileButton = (Button)findViewById(R.id.MyProfileButton);
		myProfileButton.setOnClickListener(this);
		
		Button bloodQuestionnaireButton = (Button)findViewById(R.id.BloodQuestionnaireButton);
		bloodQuestionnaireButton.setOnClickListener(this);

		Button findClinicButton = (Button)findViewById(R.id.FindClinicButton);
		findClinicButton.setOnClickListener(this);

		Button bloodLevelButton = (Button)findViewById(R.id.BloodLevelButton);
		bloodLevelButton.setOnClickListener(this);
		
		Button contactDetailsButton = (Button)findViewById(R.id.GiveBloodButton);
		contactDetailsButton.setOnClickListener(this);

	}

	// Implement the OnClickListener callback
	public void onClick(View v) {

		switch (v.getId()) {
		
		case R.id.MyProfileButton: 
			Intent intent1 = new Intent(this, MyProfileActivity.class);
			startActivity(intent1);
			break;
			
		case R.id.BloodQuestionnaireButton: 
			Intent intent2 = new Intent(this, QuestionnaireActivity.class);
			startActivity(intent2);
			break;
			
		case R.id.FindClinicButton: 
			Intent intent3 = new Intent(this, DisplayClinicsRadioActivity.class);
			startActivity(intent3);
			break;
			
		case R.id.BloodLevelButton: 
			if(CheckNetwork.isInternetAvailable(MainActivity.this)) //returns true if Internet available
			{	Intent intent4 = new Intent(this, BloodLevelsActivity.class);
				startActivity(intent4);	}   
			else
			{ Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();	}  
			break;
			
		case R.id.GiveBloodButton: 
			if(CheckNetwork.isInternetAvailable(MainActivity.this)) //returns true if internet available
			{   Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.giveblood.ie/"));
				startActivity(browserIntent); }   
			else
			{ Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();	}  
			break;
		}

	}




}
