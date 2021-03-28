package com.epsi.covidmanager.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Slot;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.beans.Vial;
import com.epsi.covidmanager.view.VaccineAdaptater;



import java.util.ArrayList;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class VaccineDashboard extends AppCompatActivity implements VaccineAdaptater.OnVaccineListener {

    private RecyclerView rv_dashboard_vaccine;
    private TextView id_alert_vaccin_names;
    private LinearLayout ly_alert_vaccins_quantity;

    private ArrayList<Vaccine> vaccines;
    private ArrayList<Slot> slots;
    private ArrayList<Vial> vials;

    private VaccineAdaptater vaccineAdaptater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vaccine_dashboard);

        vaccines = (ArrayList<Vaccine>) getIntent().getSerializableExtra("vaccines");
        slots = (ArrayList<Slot>) getIntent().getSerializableExtra("slots");
        vials = (ArrayList<Vial>) getIntent().getSerializableExtra("vials");

        id_alert_vaccin_names = findViewById(R.id.id_alert_vaccin_names);
        ly_alert_vaccins_quantity = findViewById(R.id.ly_alert_vaccins_quantity);

        rv_dashboard_vaccine = findViewById(R.id.rv_dashboard_vaccine);


        checkVaccinesQuantity();


        vaccineAdaptater = new VaccineAdaptater(vaccines, slots, vials, this);
        rv_dashboard_vaccine.setLayoutManager(new LinearLayoutManager(this));
        rv_dashboard_vaccine.setAdapter(vaccineAdaptater);
    }

    @Override
    public void onClick(Vaccine vaccine) {
        Intent intent = new Intent(this, DetailsVaccine.class);
        intent.putExtra("vaccine", vaccine);
        intent.putExtra("vaccines", vaccines);
        intent.putExtra("slots", slots);
        intent.putExtra("vials", vials);
        startActivity(intent);
    }

    public void checkVaccinesQuantity(){
        StringBuilder value = new StringBuilder();
        for(Vaccine vaccine : vaccines){
            int nb = quantityRemainToAllow(vaccine);
            if(nb < 500){
                value.append(vaccine.getName().toUpperCase()).append(" ");
            }
        }
        if(!value.toString().equals("")){
            ly_alert_vaccins_quantity.setVisibility(View.VISIBLE);
            id_alert_vaccin_names.setText(value.toString());
        }

    }

    public int quantityRemainToAllow(Vaccine vaccine){
        int nb = 0;
        for(Vial vial : vials){
            if(vial.getVaccine().getName().equals(vaccine.getName()) && vial.getSlot() == null){
                nb = nb+vial.getShotNumber();
            }
        }
        return nb;
    }
}
