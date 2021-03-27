package com.epsi.covidmanager.controller;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Vaccine;

public class formAddVaccine extends AppCompatActivity {

    public final static String VACCINE_KEY = "VACCINE_KEY";

    private TextView tv_form_vaccine_name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_vaccine);

        Vaccine vaccine = (Vaccine) getIntent().getExtras().getSerializable(VACCINE_KEY);

        tv_form_vaccine_name = findViewById(R.id.tv_form_vaccine_name);
        tv_form_vaccine_name.setText(vaccine.getName());






    }
}
