package com.epsi.covidmanager.controller;


import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;

public class DetailsSlot extends AppCompatActivity implements View.OnClickListener {

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

        vaccines = (ArrayList<Vaccine>) getIntent().getSerializableExtra("vaccines");
        slots = (ArrayList<Slot>) getIntent().getSerializableExtra("slots");
        vials = (ArrayList<Vial>) getIntent().getSerializableExtra("vials");

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
        tv_doses_prevues.setText(Integer.toString(slot.getNbInitialPlaces()));
        for (Vial vial: vials) {
            if (vial.getSlot().getId().equals(slot.getId())){
                tv_vaccin2.setText(vial.getVaccine().getName());
                break;
            }
        }

        int stock = 0;
        for (Vial vial: vials) {
            if (vial.getSlot().getId().equals(slot.getId())){
                stock += vial.getShotNumber();
            }
        }
        tv_stock.setText(Integer.toString(stock));
        tv_nb_places.setText(nbPlacesReserved + "/" + slot.getNbInitialPlaces());


    }

    @Override
    public void onClick(View v) {
        if(v == bt_less){
            nbPlacesReserved--;
            if (nbPlacesReserved == 0){
                bt_less.setEnabled(false);
            }
            else{
                bt_plus.setEnabled(true);
            }
            tv_nb_places.setText(nbPlacesReserved + "/" + slot.getNbInitialPlaces());
        }
        else if(v == bt_plus){
            nbPlacesReserved++;
            if (nbPlacesReserved == slot.getNbInitialPlaces()){
                bt_plus.setEnabled(false);
            }
            else{
                bt_less.setEnabled(true);
            }
            tv_nb_places.setText(nbPlacesReserved + "/" + slot.getNbInitialPlaces());
        }
        else if(v == bt_valider){
            Intent DashBoardActivity = new Intent(context, DashBoardActivity.class);
            DashBoardActivity.putExtra("vaccines", vaccines);
            DashBoardActivity.putExtra("slots", slots);
            DashBoardActivity.putExtra("vials", vials);
            startActivity(DashBoardActivity);
            slot.updateNbReservedPlaces(nbPlacesReserved);
        }
        else if (v == bt_modifier){
            Toast.makeText(this, "Accès à la page de modification", Toast.LENGTH_SHORT).show();
        }
        else if (v == bt_supprimer) {
            AlertDialog alertDialog = new AlertDialog.Builder(DetailsSlot.this).create();
            alertDialog.setTitle("Suppresion");
            alertDialog.setMessage("Etes vous sur de vouloir supprimer ce crénaux?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Valider",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent DashBoardActivity = new Intent(context, DashBoardActivity.class);
                            DashBoardActivity.putExtra("vaccines", vaccines);
                            DashBoardActivity.putExtra("slots", slots);
                            DashBoardActivity.putExtra("vials", vials);
                            startActivity(DashBoardActivity);
                            slot.delete();
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
            DashBoardActivity.putExtra("vaccines", vaccines);
            DashBoardActivity.putExtra("slots", slots);
            DashBoardActivity.putExtra("vials", vials);
            startActivity(DashBoardActivity);
        }
        else{
            Toast.makeText(context, "Une erreur s'est produite, veillez réessayer", Toast.LENGTH_SHORT).show();
        }
    }
}