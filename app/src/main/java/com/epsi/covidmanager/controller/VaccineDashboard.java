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
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.view.VaccineAdaptater;



import java.util.ArrayList;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class VaccineDashboard extends AppCompatActivity implements VaccineAdaptater.OnVaccineListener {

    private RecyclerView rv_dashboard_vaccine;
    private TextView id_alert_vaccin_names;
    private LinearLayout ly_alert_vaccins_quantity;

    private ArrayList<Vaccine> vaccines;

    private VaccineAdaptater vaccineAdaptater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vaccine_dashboard);

        id_alert_vaccin_names = findViewById(R.id.id_alert_vaccin_names);
        ly_alert_vaccins_quantity = findViewById(R.id.ly_alert_vaccins_quantity);

        rv_dashboard_vaccine = findViewById(R.id.rv_dashboard_vaccine);

        vaccines = new ArrayList<>();

        for(int i = 0 ; i <= 2; i++){
            vaccines.add(new Vaccine());
        }

        checkVaccinesQuantity();


        vaccineAdaptater = new VaccineAdaptater(vaccines, this);
        rv_dashboard_vaccine.setLayoutManager(new LinearLayoutManager(this));
        rv_dashboard_vaccine.setAdapter(vaccineAdaptater);

        testBcrypt();



    }

    @Override
    public void onClick(Vaccine vaccine) {
        Intent intent = new Intent(this, DetailsVaccine.class);
        intent.putExtra(DetailsVaccine.VACCINE_KEY, vaccine);
        startActivity(intent);
    }

    public void checkVaccinesQuantity(){
        String value = "";
        for(Vaccine vaccine : vaccines){
            if(vaccine.getNbPrev() < 500){
                value += vaccine.getName().toUpperCase()+" ";
            }
        }
        if(value != ""){
            ly_alert_vaccins_quantity.setVisibility(View.VISIBLE);
            id_alert_vaccin_names.setText(value);
        }


    }

    private void testBcrypt(){
        String password = "1234";
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), bcryptHashString);
        if(result.verified == true){ Toast.makeText(this, "Bcrypt marche", Toast.LENGTH_SHORT).show();  }
        else{
            Toast.makeText(this, "Bcrypt marche pas", Toast.LENGTH_SHORT).show();
        }

    }
}
