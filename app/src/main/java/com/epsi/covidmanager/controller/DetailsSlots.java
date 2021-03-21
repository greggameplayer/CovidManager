package com.epsi.covidmanager.controller;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Slot;

public class DetailsSlots extends AppCompatActivity {

    public final static String SLOT_KEY = "SLOT_KEY";


    //private TextView tv_activity_name, tv_activity_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.slot_details);

        //tv_activity_name = (TextView) findViewById(R.id.tv_activity_name);
        //tv_activity_info = (TextView) findViewById(R.id.tv_activity_info);

        Slot slot = (Slot) getIntent().getExtras().getSerializable(SLOT_KEY);

        //tv_activity_name.setText(fields.getAdresse() + " " +fields.getCommune());
        //tv_activity_info.setText();
    }
}
