package com.epsi.covidmanager.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Slot;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.beans.Vial;
import com.parse.ParseException;

import java.util.ArrayList;

public class formAddVaccine extends AppCompatActivity implements View.OnClickListener {


    private Vaccine vaccine;
    private int nbVial, nbDose;

    private ArrayList<Vaccine> vaccines;
    private ArrayList<Slot> slots;
    private ArrayList<Vial> vials;


    private TextView tv_form_vaccine_name;
    private EditText et_form_vaccine_nbVial, et_form_vaccine_nbDose;
    private Button bt_form_vaccine_add, bt_form_vaccine_return;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_vaccine);

        vaccine = (Vaccine) getIntent().getExtras().getSerializable("vaccine");

        vaccines = (ArrayList<Vaccine>) getIntent().getSerializableExtra("vaccines");
        slots = (ArrayList<Slot>) getIntent().getSerializableExtra("slots");
        vials = (ArrayList<Vial>) getIntent().getSerializableExtra("vials");

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
            if(TextUtils.isEmpty(et_form_vaccine_nbDose.getText().toString()) || TextUtils.isEmpty(et_form_vaccine_nbVial.getText().toString()) ){
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
            else{
                onAdd();
                onReturn();
            }
        }else{
            onReturn();
        }

    }

    private void onReturn(){
        Intent intent = new Intent(this, DetailsVaccine.class);
        intent.putExtra("vaccine", vaccine);
        intent.putExtra("vaccines", vaccines);
        intent.putExtra("slots", slots);
        intent.putExtra("vials", vials);
        startActivity(intent);
    }
    private void onAdd(){

        ArrayList<Vial> vialsBis = new ArrayList<>();

        nbVial = Integer.parseInt(et_form_vaccine_nbVial.getText().toString());
        nbDose = Integer.parseInt(et_form_vaccine_nbDose.getText().toString());


        for(int i = 0 ; i<= nbVial ; i++){
            vialsBis.add(new Vial(nbDose, vaccine));
        }

        for(int i = 0 ; i<= vialsBis.size() ; i++){
           try {
               Vial.insert(vialsBis.get(i), this);
               vials.add(vialsBis.get(i));
           }
           catch (Exception e){
               Toast.makeText(this, "L'insertion de données a échoué", Toast.LENGTH_SHORT).show();
           }
        }

    }
}
