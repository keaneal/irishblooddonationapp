package com.hotmail.keanser.irishblooddonationapp.bloodlevels;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hotmail.keanser.irishblooddonationapp.R;

public class BloodLevelsAdapter extends BaseAdapter{
    private Context context;

    private ArrayList<BloodLevels> listBloodLevels;

    public BloodLevelsAdapter(Context context, ArrayList<BloodLevels> listBloodLevels) {
        this.context = context;
        this.listBloodLevels = listBloodLevels;
    }

    public int getCount() {
        return listBloodLevels.size();
    }

    public Object getItem(int position) {
        return listBloodLevels.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        BloodLevels bloodLevels = listBloodLevels.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_blood_levels, null);
        }
        TextView blBloodType = (TextView) convertView.findViewById(R.id.blBloodType);
        blBloodType.setText(bloodLevels.getBloodType());

        TextView blBloodLevel = (TextView) convertView.findViewById(R.id.blBloodLevel);
        blBloodLevel.setText(bloodLevels.getBloodLevel());

        return convertView;
    }

}

