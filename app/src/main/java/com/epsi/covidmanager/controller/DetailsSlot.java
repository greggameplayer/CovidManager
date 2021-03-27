package com.epsi.covidmanager.controller;


import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog;
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
    private Button bt_modifier, bt_supprimer, bt_retour;
    private Context context;

    //Donnees
    private ArrayList<Slot> slots;
    private ArrayList<Vial> vials;
    private ArrayList<Vaccine> vaccines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_slot);

        context = this;

        vaccines = (ArrayList<Vaccine>) getIntent().getSerializableExtra("vaccines");
        slots = (ArrayList<Slot>) getIntent().getSerializableExtra("slots");
        vials = (ArrayList<Vial>) getIntent().getSerializableExtra("vials");

        Slot slot = (Slot) getIntent().getExtras().getSerializable(SLOT_KEY);
        bt_modifier = findViewById(R.id.bt_modifier);
        bt_supprimer = findViewById(R.id.bt_supprimer);
        bt_retour = findViewById(R.id.bt_retour);
        bt_modifier.setOnClickListener(this);
        bt_supprimer.setOnClickListener(this);
        bt_retour.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == bt_modifier){
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
                            //suppression
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