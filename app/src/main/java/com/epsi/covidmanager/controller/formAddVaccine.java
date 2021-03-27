package com.epsi.covidmanager.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Vaccine;

public class formAddVaccine extends AppCompatActivity implements View.OnClickListener {

    public final static String VACCINE_KEY = "VACCINE_KEY";

    private Vaccine vaccine;

    private TextView tv_form_vaccine_name;
    private EditText et_form_vaccine_nbVial, et_form_vaccine_nbDose;
    private Button bt_form_vaccine_add, bt_form_vaccine_return;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_vaccine);

        Vaccine vaccine = (Vaccine) getIntent().getExtras().getSerializable(VACCINE_KEY);

        et_form_vaccine_nbVial = findViewById(R.id.et_form_vaccine_nbVial);
        et_form_vaccine_nbDose = findViewById(R.id.et_form_vaccine_nbDose);
        tv_form_vaccine_name = findViewById(R.id.tv_form_vaccine_name);
        tv_form_vaccine_name.setText(vaccine.getName());

        bt_form_vaccine_add = findViewById(R.id.bt_form_vaccine_add);
        bt_form_vaccine_return = findViewById(R.id.bt_form_vaccine_return);

        bt_form_vaccine_return.setOnClickListener(this);
        bt_form_vaccine_add.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == bt_form_vaccine_add.getId()){
            Toast.makeText(this, "TO do", Toast.LENGTH_SHORT).show();
        }else{
            onReturn();
        }

    }

    private void onReturn(){
        Intent intent = new Intent(this, DetailsVaccine.class);
        intent.putExtra(DetailsVaccine.VACCINE_KEY, vaccine);
        startActivity(intent);
    }
}
