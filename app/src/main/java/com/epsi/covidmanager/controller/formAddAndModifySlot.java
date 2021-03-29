package com.epsi.covidmanager.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Slot;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.beans.Vial;

import java.util.ArrayList;

public class formAddAndModifySlot extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, TextView.OnEditorActionListener{

    private Spinner spinnerVaccin;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private EditText heureDebut, heureFin, nbDose;
    private Button bt_valider;
    private String vaccin;
    private ArrayList<Slot> slots;
    private ArrayList<Vial> vials;
    private ArrayList<Vaccine> vaccines;

    private Slot slot;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_slot);

        vaccines = (ArrayList<Vaccine>) getIntent().getSerializableExtra("vaccines");
        slots = (ArrayList<Slot>) getIntent().getSerializableExtra("slots");
        vials = (ArrayList<Vial>) getIntent().getSerializableExtra("vials");

        ArrayList<String> tabNameVaccines = new ArrayList<>();

        for (Vaccine vaccine : vaccines){
            tabNameVaccines.add(vaccine.getName());
        }

        spinnerVaccin = (Spinner) findViewById(R.id.spinnerVaccin);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(formAddAndModifySlot.this,
                android.R.layout.simple_spinner_item, tabNameVaccines);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVaccin.setAdapter(adapter);
        spinnerVaccin.setOnItemSelectedListener(this);

        timePicker = findViewById(R.id.timePicker);
        datePicker = findViewById(R.id.datePicker);
        timePicker.setIs24HourView(true);

        heureDebut = findViewById(R.id.heureDebut);
        heureFin = findViewById(R.id.heureFin);
        nbDose = findViewById(R.id.nombreDose);
        bt_valider = findViewById(R.id.bt_valider);

        slot = (Slot) getIntent().getSerializableExtra("slot");

        int position = 0;
        if (slot!=null) {
            for (Vial vial : vials){
                if(vial.getSlot().getId().equals(slot.getId())){
                    position = adapter.getPosition(vial.getVaccine().getName());
                    break;
                }
            }
            spinnerVaccin.setSelection(position, true);
            heureDebut.setOnEditorActionListener(this);
            heureFin.setOnEditorActionListener(this);
            nbDose.setText(Integer.toString(slot.getNbInitialPlaces()));
        }

        bt_valider.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

   @Override
    public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
        Log.w("test123", "marche");
         if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
            }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        vaccin = (String) parent.getItemAtPosition(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
       String heureDebutStr = heureDebut.getText().toString();
       String heureFinStr = heureFin.getText().toString();
       String nombreDoseStr = nbDose.getText().toString();
       //BDD(heureDebutStr, heureFinStr, nombreDoseStr, vaccin);
    }

}

