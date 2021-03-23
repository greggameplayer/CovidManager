package com.epsi.covidmanager.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.epsi.covidmanager.R;

public class DashBoardVaccin extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private Spinner spinnerVaccin;
    private static final String[] paths = {"Astrazeneca", "Pfizer", "Moderna"};
    EditText heureDebut, heureFin, nbDose;
    Button btn_valider;
    String vaccin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_vaccin);

        Intent intent = getIntent();

        String str = "";
        if (intent.hasExtra("slot")){
            str = intent.getStringExtra("slot"); // on récupère la valeur associée à la clé
        }

        spinnerVaccin = (Spinner) findViewById(R.id.spinnerVaccin);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DashBoardVaccin.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVaccin.setAdapter(adapter);
        spinnerVaccin.setOnItemSelectedListener(this);

        heureDebut = findViewById(R.id.heureDebut);
        heureFin = findViewById(R.id.heureFin);
        nbDose = findViewById(R.id.nombreDose);

        btn_valider.setOnClickListener((View.OnClickListener) this);

        if (str == "editSlot") {
            //TODO requete SQL slot

           // heureDebut.setText();
           // heureFin.setText();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                vaccin = "Astrazeneca";
                break;
            case 1:
                vaccin = "Pfizer";
                break;
            case 2:
                vaccin = "Moderna";
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        String heureDebutStr = heureDebut.getText().toString();
        String heureFinStr = heureFin.getText().toString();
        String nombreDoseStr = nbDose.getText().toString();

        BDD(heureDebutStr, heureFinStr, nombreDoseStr, vaccin);
    }

    public void BDD(String heureDebut, String heureFin, String nombreDose,String vaccin){
        //TODO créer les requetes d'ajout de crénaud
        String[] slot = new String[3];
        //Verify if the slot is disponible
        //Requete SQL that select a slot, if there is none it can add it
        //TODO

        if (slot.length == 0){
            //requete wich add the new slot
            //TODO
            Intent intent = new Intent(this, DashBoardActivity.class);
            startActivity(intent);
        }

        else {
            Toast.makeText(this, "Le crénaud horaire est déjà pris", Toast.LENGTH_LONG).show();
        }

    }
}
