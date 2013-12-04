package com.hotmail.keanser.irishblooddonationapp.findclinic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hotmail.keanser.irishblooddonationapp.R;

public class DisplayClinicsRadioActivity extends Activity {

	private RadioGroup radioCountyGroup;
	private RadioButton radioCountyButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_clinics_radio);
		
	}
	
	public void onRadioButtonClicked(View view) {	
		
		// store the text corresponding to the RadioButton which is clicked
		// initialize the string variable
		String link = null;
		
		radioCountyGroup = (RadioGroup) findViewById(R.id.radioGroupCounties);

		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();
		// Check which radio button was clicked
		switch (view.getId()) {
		case R.id.radioCarlow:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Carlow);
			break;
		case R.id.radioCavan:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Cavan);
			break;
		case R.id.radioClare:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Clare);
			break;
		case R.id.radioCork:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Cork);
			break;
		case R.id.radioDonegal:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Donegal);
			break;
		case R.id.radioDublinCity:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.DublinCity);
			break;
		case R.id.radioDublinNorth:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.DublinNorth);
			break;
		case R.id.radioGalway:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Galway);
			break;
		case R.id.radioKerry:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Kerry);
			break;
		case R.id.radioKildare:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Kildare);
			break;
		case R.id.radioKilkenny:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Kilkenny);
			break;
		case R.id.radioLaois:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Laois);
			break;
		case R.id.radioLeitrim:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Leitrim);
			break;
		case R.id.radioLimerick:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Limerick);
			break;
		case R.id.radioLongford:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Longford);
			break;
		case R.id.radioLouth:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Louth);
			break;
			
		case R.id.radioMayo:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Mayo);
			break;
		case R.id.radioMeath:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Meath);
			break;
		case R.id.radioMonaghan:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Monaghan);
			break;
		case R.id.radioOffaly:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Offaly);
			break;
		case R.id.radioRoscommon:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Roscommon);
			break;
		case R.id.radioSligo:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Sligo);
			break;
		case R.id.radioTipperary:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Tipperary);
			break;
		case R.id.radioWaterford:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Waterford);
			break;
		case R.id.radioWestmeath:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Westmeath);
			break;
		case R.id.radioWexford:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Wexford);
			break;
		case R.id.radioWicklow:
			if (checked)
				link = DisplayClinicsRadioActivity.this.getResources().getString(R.string.Wicklow);
			break;			
		}

		// get selected radio button from radioGroup
		int selectedId = radioCountyGroup.getCheckedRadioButtonId();

		// find the radiobutton by returned id
		radioCountyButton = (RadioButton) findViewById(selectedId);		
		
		Intent intent = new Intent(this, DisplayClinicsListActivity.class);
		intent.putExtra("radioChosen", link); // pass link to the next Activity
		intent.putExtra("radioCountySelected", radioCountyButton.getText()); // pass button text to the next activity
		startActivity(intent);
	}

}
