package com.epsi.covidmanager.controller;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Slot;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.beans.Vial;
import com.epsi.covidmanager.model.webservice.APIService;
import com.epsi.covidmanager.model.webservice.RetrofitHttpUtilis;
import com.epsi.covidmanager.view.VaccineAdaptater;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsSlot extends AppCompatActivity implements View.OnClickListener  {

    public final static String SLOT_KEY = "SLOT_KEY";

    //Composoants graphiques
    private TextView tv_slot_details_date, tv_doses_prevues, tv_vaccin2, tv_stock, tv_nb_places;
    private Button bt_less, bt_plus, bt_valider, bt_modifier, bt_supprimer, bt_retour;
    private Context context;
    private int nbPlacesReserved;
    private Slot slot;

    //Donnees
    private ArrayList<Slot> slots;
    private ArrayList<Vial> vials;
    private ArrayList<Vaccine> vaccines;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_slot);

        context = this;

        slots = (ArrayList<Slot>) getIntent().getSerializableExtra("slots");
        vials = (ArrayList<Vial>) getIntent().getSerializableExtra("vials");

        loadVaccines();

        slot = (Slot) getIntent().getExtras().getSerializable(SLOT_KEY);
        nbPlacesReserved = slot.getNbReservedPlaces();

        tv_slot_details_date = findViewById(R.id.tv_slot_details_date);
        tv_doses_prevues = findViewById(R.id.tv_doses_prevues);
        tv_vaccin2 = findViewById(R.id.tv_vaccin2);
        tv_stock = findViewById(R.id.tv_stock);
        tv_nb_places = findViewById(R.id.tv_nb_places);

        bt_less = findViewById(R.id.bt_less);
        if(nbPlacesReserved == 0){
            bt_less.setEnabled(false);
        }
        bt_plus = findViewById(R.id.bt_plus);
        if(nbPlacesReserved == slot.getNbInitialPlaces()){
            bt_plus.setEnabled(false);
        }
        bt_valider = findViewById(R.id.bt_valider);
        bt_modifier = findViewById(R.id.bt_modifier);
        bt_supprimer = findViewById(R.id.bt_supprimer);
        bt_retour = findViewById(R.id.bt_retour);

        bt_less.setOnClickListener(this);
        bt_plus.setOnClickListener(this);
        bt_valider.setOnClickListener(this);
        bt_modifier.setOnClickListener(this);
        bt_supprimer.setOnClickListener(this);
        bt_retour.setOnClickListener(this);

        tv_slot_details_date.setText(slot.getDates());
        tv_doses_prevues.setText(tv_doses_prevues.getText() + Integer.toString(slot.getNbInitialPlaces()));

        for (Vial vial: vials) {
            if (vial.getSlot() != null && vial.getSlot().getId().equals(slot.getId())){
                tv_vaccin2.setText( tv_vaccin2.getText() + vial.getVaccine().getName());
                break;
            }
        }

        int stock = 0;
        for (Vial vial: vials) {
            if (vial.getSlot() != null) {
            }
            if (vial.getSlot() != null && vial.getSlot().getId().equals(slot.getId())){
                stock = Integer.sum(stock, vial.getShotNumber());
            }
        }
        tv_stock.setText(tv_stock.getText() + Integer.toString(stock));
        tv_nb_places.setText(nbPlacesReserved + "/" + slot.getNbInitialPlaces());
        bt_valider.setEnabled(false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if(v == bt_less){
            nbPlacesReserved--;
            if (nbPlacesReserved == slot.getNbReservedPlaces()) {
                bt_valider.setEnabled(false);
            } else if (nbPlacesReserved == 0){
                bt_valider.setEnabled(true);
                bt_less.setEnabled(false);
            }
            else{
                bt_valider.setEnabled(true);
                bt_plus.setEnabled(true);
            }
            tv_nb_places.setText(nbPlacesReserved + "/" + slot.getNbInitialPlaces());
        }
        else if(v == bt_plus){
            nbPlacesReserved++;
            if (nbPlacesReserved == slot.getNbReservedPlaces()) {
                bt_valider.setEnabled(false);
            } else if (nbPlacesReserved == slot.getNbInitialPlaces()){
                bt_plus.setEnabled(false);
                bt_valider.setEnabled(true);
            }
            else{
                bt_less.setEnabled(true);
                bt_valider.setEnabled(true);
            }
            tv_nb_places.setText(nbPlacesReserved + "/" + slot.getNbInitialPlaces());
        }
        else if(v == bt_valider){
            for (Slot slot1 : slots) {
                if (slot1.getId().equals(slot.getId())) {
                    //TODO: revoir format startTime et endTime coté API ou voir pour une convertion en String avant d'envoie
                    slot1.setNbReservedPlaces(nbPlacesReserved);
                    APIService apiService = RetrofitHttpUtilis.getRetrofitInstance().create(APIService.class);
                    apiService.updateSlot(slot1.getId(), slot1).enqueue(new Callback<Slot>() {
                        @Override
                        public void onResponse(Call<Slot> call, Response<Slot> response) {
                            Intent DashBoardActivity = new Intent(context, DashBoardActivity.class);
                            startActivity(DashBoardActivity);
                        }

                        @Override
                        public void onFailure(Call<Slot> call, Throwable t) {
                            Log.w("TAGI", t.getMessage());
                        }
                    });
                    break;
                }
            }
        }
        else if (v == bt_modifier){
            Intent intent = new Intent(this, formAddAndModifySlot.class);
            intent.putExtra("slot", slot);
            intent.putExtra("vaccines", vaccines);
            intent.putExtra("slots", slots);
            intent.putExtra("vials", vials);

            startActivity(intent);
        }
        else if (v == bt_supprimer) {
            AlertDialog alertDialog = new AlertDialog.Builder(DetailsSlot.this).create();
            alertDialog.setTitle("Suppresion");
            alertDialog.setMessage("Etes vous sur de vouloir supprimer ce crénaux?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Valider",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            for (Slot slot1 : slots) {
                                if (slot1.getId().equals(slot.getId())) {
                                    for (Vial vial: vials) {
                                        if (vial.getSlot() != null && vial.getSlot().getId().equals(slot1.getId())) {
                                            APIService apiService = RetrofitHttpUtilis.getRetrofitInstance().create(APIService.class);

                                            apiService.deleteSlot(slot1.getId()).enqueue(new Callback<Slot>() {
                                                @Override
                                                public void onResponse(Call<Slot> call, Response<Slot> response) {
                                                    Intent DashBoardActivity = new Intent(context, DashBoardActivity.class);
                                                    startActivity(DashBoardActivity);
                                                }

                                                @Override
                                                public void onFailure(Call<Slot> call, Throwable t) {
                                                    Log.w("TAGI", t.getMessage());
                                                }
                                            });
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Annuler",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "La suppression a bien été annuler", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else if (v == bt_retour) {
            Intent DashBoardActivity = new Intent(context, DashBoardActivity.class);
            startActivity(DashBoardActivity);
        }
        else{
            Toast.makeText(context, "Une erreur s'est produite, veillez réessayer", Toast.LENGTH_SHORT).show();
        }
    }


    public void loadVaccines(){

        APIService apiService = RetrofitHttpUtilis.getRetrofitInstance().create(APIService.class);

        apiService.getVaccines().enqueue(new Callback<List<Vaccine>>() {
            @Override
            public void onResponse(Call<List<Vaccine>> call, Response<List<Vaccine>> response) {
                vaccines = (ArrayList<Vaccine>) response.body();
            }

            @Override
            public void onFailure(Call<List<Vaccine>> call, Throwable t) {
                Log.w("TAGI", t.getMessage());
            }
        });
    }
}