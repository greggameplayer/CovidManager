package com.epsi.covidmanager.controller;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Vaccine;



public class DetailsVaccine extends AppCompatActivity implements View.OnClickListener {

    public final static String VACCINE_KEY = "VACCINE_KEY";
    private Vaccine vaccine;

    private Button bt_details_vaccine_add, bt_details_vaccine_return;
    private TextView tv_details_vaccine_quantity_prev, tv_details_vaccine_quantity_remain, tv_details_vaccine_quantity, tv_detail_vaccine_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_vaccine);

        vaccine = (Vaccine) getIntent().getExtras().getSerializable(VACCINE_KEY);

        tv_detail_vaccine_name = findViewById(R.id.tv_detail_vaccine_name);
        tv_details_vaccine_quantity_prev = findViewById(R.id.tv_details_vaccine_quantity_prev);
        tv_details_vaccine_quantity_remain = findViewById(R.id.tv_details_vaccine_quantity_remain);
        tv_details_vaccine_quantity = findViewById(R.id.tv_details_vaccine_quantity);
        bt_details_vaccine_add = findViewById(R.id.bt_details_vaccine_add);
        bt_details_vaccine_return = findViewById(R.id.bt_details_vaccine_return);


        tv_detail_vaccine_name.setText(vaccine.getName());
        tv_details_vaccine_quantity_prev.setText("Ajouter");
        tv_details_vaccine_quantity_remain.setText("Réfléchir sur la notion en groupe");
        tv_details_vaccine_quantity.setText("Ajouter");

        bt_details_vaccine_add.setOnClickListener(this);
        bt_details_vaccine_return.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == bt_details_vaccine_add.getId()){
            Intent intent = new Intent(this, formAddVaccine.class);
            intent.putExtra(formAddVaccine.VACCINE_KEY, vaccine);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, VaccineDashboard.class);
            startActivity(intent);
        }

    }
}
