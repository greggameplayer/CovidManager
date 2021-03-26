package com.epsi.covidmanager.controller;


import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Vaccine;



public class DetailsVaccine extends AppCompatActivity {

    public final static String VACCINE_KEY = "VACCINE_KEY";

    private Button bt_details_vaccine_add;
    private TextView tv_details_vaccine_quantity_prev, tv_details_vaccine_quantity_remain, tv_details_vaccine_quantity, tv_detail_vaccine_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_vaccine);

        Vaccine vaccine = (Vaccine) getIntent().getExtras().getSerializable(VACCINE_KEY);

        tv_detail_vaccine_name = findViewById(R.id.tv_detail_vaccine_name);
        tv_details_vaccine_quantity_prev = findViewById(R.id.tv_details_vaccine_quantity_prev);
        tv_details_vaccine_quantity_remain = findViewById(R.id.tv_details_vaccine_quantity_remain);
        tv_details_vaccine_quantity = findViewById(R.id.tv_details_vaccine_quantity);


        tv_detail_vaccine_name.setText(vaccine.getName());
        tv_details_vaccine_quantity_prev.setText(vaccine.getNb()+"");
        tv_details_vaccine_quantity_remain.setText("Réfléchir sur la notion en groupe");
        tv_details_vaccine_quantity.setText(vaccine.getNbPrev()+"");


    }

}
