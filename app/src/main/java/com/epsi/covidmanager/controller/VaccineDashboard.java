package com.epsi.covidmanager.controller;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.view.VaccineAdaptater;

import java.util.ArrayList;

public class VaccineDashboard extends AppCompatActivity implements VaccineAdaptater.OnVaccineListener {

    private RecyclerView rv_dashboard_vaccine;

    private ArrayList<Vaccine> vaccines;

    private VaccineAdaptater vaccineAdaptater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vaccine_dashboard);

        rv_dashboard_vaccine = findViewById(R.id.rv_dashboard_vaccine);

        vaccines = new ArrayList<>();

        for(int i = 0 ; i <= 2; i++){
            vaccines.add(new Vaccine());
        }
        vaccineAdaptater = new VaccineAdaptater(vaccines, this);
        rv_dashboard_vaccine.setLayoutManager(new LinearLayoutManager(this));
        rv_dashboard_vaccine.setAdapter(vaccineAdaptater);

    }

    @Override
    public void onClick(Vaccine vaccine) {
        Log.w("TAG", "Ca marche");
    }
}
