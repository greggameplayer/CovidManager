package com.epsi.covidmanager.controller;


import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.mongo.Slot;



public class DetailsVaccine extends AppCompatActivity {

    public final static String VACCINE_KEY = "VACCINE_KEY";

    private TextView tv_detail_vaccine_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_vaccine);

        Vaccine vaccine = (Vaccine) getIntent().getExtras().getSerializable(VACCINE_KEY);

        tv_detail_vaccine_name = findViewById(R.id.tv_detail_vaccine_name);
        tv_detail_vaccine_name.setText(vaccine.getName());




    }

}
