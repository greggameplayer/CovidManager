package com.epsi.covidmanager.controller;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.beans.Vial;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DetailsVaccine extends AppCompatActivity implements View.OnClickListener {

    private Vaccine vaccine;

    private ArrayList<Vaccine> vaccines;
    private ArrayList<Vial> vials;

    private Button bt_details_vaccine_add, bt_details_vaccine_return;
    private TextView tv_details_vaccine_quantity_prev, tv_details_vaccine_quantity_remain, tv_details_vaccine_quantity, tv_detail_vaccine_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_vaccine);
        vaccine = (Vaccine) getIntent().getExtras().getSerializable("vaccine");


        vaccines = (ArrayList<Vaccine>) getIntent().getSerializableExtra("vaccines");
        vials = (ArrayList<Vial>) getIntent().getSerializableExtra("vials");

        tv_detail_vaccine_name = findViewById(R.id.tv_detail_vaccine_name);
        tv_details_vaccine_quantity_prev = findViewById(R.id.tv_details_vaccine_quantity_prev);
        tv_details_vaccine_quantity_remain = findViewById(R.id.tv_details_vaccine_quantity_remain);
        tv_details_vaccine_quantity = findViewById(R.id.tv_details_vaccine_quantity);
        bt_details_vaccine_add = findViewById(R.id.bt_details_vaccine_add);
        bt_details_vaccine_return = findViewById(R.id.bt_details_vaccine_return);


        tv_detail_vaccine_name.setText(vaccine.getName());
        tv_details_vaccine_quantity_prev.setText(quantityRemainToDistribute(vaccine) + "");
        tv_details_vaccine_quantity_remain.setText(quantityRemainToAllow(vaccine) + "");
        tv_details_vaccine_quantity.setText(quantityAllTime(vaccine) + "");

        bt_details_vaccine_add.setOnClickListener(this);
        bt_details_vaccine_return.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == bt_details_vaccine_add.getId()){
            Intent intent = new Intent(this, formAddVaccine.class);
            intent.putExtra("vaccine", vaccine);
            intent.putExtra("vaccines", vaccines);
            intent.putExtra("vials", vials);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, VaccineDashboard.class);
            startActivity(intent);
        }

    }

    public int quantityAllTime(Vaccine vaccine){
        int nb = 0;
        if (vials != null) {
            for (Vial vial : vials) {
                if (vial.getVaccine().getName().equals(vaccine.getName())) {
                    nb += vial.getShotNumber();
                }
            }
        }
        return nb;
    }

    public int quantityRemainToAllow(Vaccine vaccine){
        int nb = 0;
        if (vials != null) {
            for (Vial vial : vials) {
                if (vial.getVaccine().getName().equals(vaccine.getName()) && vial.getSlot() == null) {
                    nb += vial.getShotNumber();
                }
            }
        }
        return nb;
    }

    public int quantityRemainToDistribute(Vaccine vaccine){
        int nb = 0;
        Date currentTime = Calendar.getInstance().getTime(), date;
        long date1, date2;

        date2 = currentTime.getTime();
        if (vials != null) {
            for (Vial vial : vials) {

                if (vial.getSlot() != null) {
                    date = vial.getSlot().getStartTime();
                    date1 = date.getTime();

                    long longDate = date.getTime();

                    if (vial.getVaccine().getName().equals(vaccine.getName()) && date1 > date2) {
                        nb += vial.getShotNumber();
                    }
                } else {
                    if (vial.getVaccine().getName().equals(vaccine.getName())) {
                        nb += vial.getShotNumber();
                    }
                }
            }
        }
        return nb;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "It's impossible to go backward", Toast.LENGTH_SHORT).show();
        moveTaskToBack(false);
    }
}
