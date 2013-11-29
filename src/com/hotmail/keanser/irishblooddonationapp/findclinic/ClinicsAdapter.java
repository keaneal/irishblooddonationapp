package com.hotmail.keanser.irishblooddonationapp.findclinic;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hotmail.keanser.irishblooddonationapp.R;

public class ClinicsAdapter extends BaseAdapter {
	private Context context;

	private ArrayList<Clinics> listClinics;

	public ClinicsAdapter(Context context, ArrayList<Clinics> listClinics) {
		this.context = context;
		this.listClinics = listClinics;
	}

	public int getCount() {
		return listClinics.size();
	}

	public Object getItem(int position) {
		return listClinics.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup viewGroup) {
		Clinics clinics = listClinics.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater
					.inflate(R.layout.activity_display_clinics_list_contents, null);
		}
		TextView clinicDetails = (TextView) convertView
				.findViewById(R.id.clinicDetails);
		clinicDetails.setText(clinics.getClinicDetails());

		TextView clinicLink = (TextView) convertView
				.findViewById(R.id.clinicLink);
		clinicLink.setText(clinics.getClinicLink());

		return convertView;
	}

}
