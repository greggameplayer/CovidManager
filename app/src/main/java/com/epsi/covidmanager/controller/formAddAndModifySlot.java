package com.epsi.covidmanager.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Slot;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.beans.Vial;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class formAddAndModifySlot extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner spinnerVaccin;
    private EditText heureDebut, heureFin, nbDose;
    private Button bt_valider, bt_retour;
    private String vaccin;
    private ArrayList<Slot> slots;
    private ArrayList<Vial> vials;
    private ArrayList<Vaccine> vaccines;
    private AwesomeValidation heureValidation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
    private SimpleDateFormat dateFormat;
    private final Object lockObject = new Object();

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

        for (Vaccine vaccine : vaccines) {
            for (Vial vial : vials) {
                if (vial.getVaccine().getName().equals(vaccine.getName())) {
                    tabNameVaccines.add(vaccine.getName());
                    break;
                }
            }
        }

        spinnerVaccin = (Spinner) findViewById(R.id.spinnerVaccin);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(formAddAndModifySlot.this,
                android.R.layout.simple_spinner_item, tabNameVaccines);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVaccin.setAdapter(adapter);
        spinnerVaccin.setOnItemSelectedListener(this);

        heureDebut = findViewById(R.id.heureDebut);
        heureValidation.setContext(this);
        heureValidation.setUnderlabelColorByResource(R.color.red);
        heureValidation.addValidation(this, R.id.heureDebut, "^([1-9]|([012][0-9])|(3[01]))\\/([0]{0,1}[1-9]|1[012])\\/\\d\\d\\d\\d\\s([0-1]?[0-9]|2?[0-3]):([0-5]\\d)$", R.string.wrong_format);
        heureValidation.addValidation(this, R.id.heureFin, "^([1-9]|([012][0-9])|(3[01]))\\/([0]{0,1}[1-9]|1[012])\\/\\d\\d\\d\\d\\s([0-1]?[0-9]|2?[0-3]):([0-5]\\d)$", R.string.wrong_format);

        heureFin = findViewById(R.id.heureFin);
        nbDose = findViewById(R.id.nombreDose);
        bt_valider = findViewById(R.id.bt_valider);
        bt_retour = findViewById(R.id.bt_retour1);
        dateFormat = new SimpleDateFormat("d/MM/YYYY hh:mm");

        slot = (Slot) getIntent().getSerializableExtra("slot");

        int position = 0;
        if (slot != null) {
            for (Vial vial : vials) {
                if (vial.getSlot().getId().equals(slot.getId())) {
                    position = adapter.getPosition(vial.getVaccine().getName());
                    break;
                }
            }
            spinnerVaccin.setSelection(position, true);
            nbDose.setText(Integer.toString(slot.getNbInitialPlaces()));
            heureDebut.setText(dateFormat.format(slot.getStartTime()));
            heureFin.setText(dateFormat.format(slot.getEndTime()));
        }

        bt_valider.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
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

        if (v == bt_valider) {
            if (heureValidation.validate()) {
                ArrayList<Vial> vialsWhoseNewValue = new ArrayList<>();
                boolean isPossible = true;
                int totalDoses = 0;

                String vaccinStr = vaccin;
                int nombreDoseStr = Integer.parseInt(nbDose.getText().toString());
                Date heureDebutStr = null;
                Date heureFinStr = null;

                try {
                    heureDebutStr = dateFormat.parse(String.valueOf(heureDebut.getText()));
                    heureFinStr = dateFormat.parse(String.valueOf(heureFin.getText()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                for (Vial vial : vials) {
                    if (vial.getSlot() != null) {
                        if (vial.getVaccine().getName().equals(vaccinStr)) {
                            totalDoses += vial.getShotNumber();
                            if (nombreDoseStr <= 0 || nombreDoseStr > totalDoses) {
                                Toast.makeText(this, "Le nombre de doses entrées est supérieur au stock actuel (" + totalDoses + ")", Toast.LENGTH_LONG).show();
                                isPossible = false;
                                break;
                            }
                        }

                        if ((vial.getSlot().getStartTime().after(heureDebutStr) && vial.getSlot().getEndTime().before(heureDebutStr)) || (vial.getSlot().getStartTime().after(heureFinStr) && vial.getSlot().getEndTime().before(heureFinStr))) {
                            Toast.makeText(this, "Les différents créneaux ne peuvent pas se superposer ( créneaux existant concerné :" + vial.getSlot().getStartTime() + " - " + vial.getSlot().getEndTime() + ")", Toast.LENGTH_LONG).show();
                            isPossible = false;
                            break;
                        }
                    }
                }
                if (isPossible) {
                    Log.d("possible", "possible");
                    if (slot != null) {
                        for (Slot slot1 : slots) {
                            if (slot1.getId().equals(slot.getId())) {
                                for (Vial vial : vials) {
                                    Log.d("vaccin", vaccinStr);
                                    if (vial.getSlot() != null && vial.getVaccine().getName().equals(vaccinStr) && vial.getSlot().equals(slot1)) { // Slot déjà attribué
                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Slot");

                                        // Retrieve the object by id

                                        vial.getSlot().setStartTime(heureDebutStr);
                                        vial.getSlot().setEndTime(heureFinStr);
                                        vial.getSlot().setNbReservedPlaces(nombreDoseStr);
                                        for (Vial vial2 : vials) {
                                            vial2.setSlot(null);
                                            vial2.removeSlot(vial.getVaccine().getId(), ele -> {
                                            });
                                        }
                                        Log.d("good", "done");
                                        Intent intent = new Intent(this, DashBoardActivity.class);
                                        intent.putExtra("vaccines", vaccines);
                                        intent.putExtra("slots", slots);
                                        intent.putExtra("vials", vials);
                                        startActivity(intent);
                                        //update des vials
                                        //for (Vial vial1 : vials) {
                                        //    if (vial1.getShotNumber() > totalDoses) {
                                        //        totalDoses -= vial1.getShotNumber();
                                        //    }
                                        //}
                                        //    if(totalDoses != 0){
                                        //        Toast.makeText(this, "Un nombre total de " + nombreDoseStr + " n'est pas atteignable de façon exacte ( trop plein de :" + totalDoses + ")", Toast.LENGTH_LONG).show();
                                        //    }
                                        //    vialsWhoseNewValue.add(vial1);
                                        //    else{
                                        //        //Vial.removeSlot();
                                        //        ArrayList<Vial> vialsBis = new ArrayList<>();
                                        //        for ( Vial upVial : vials){
                                        //            if (slot != null){
                                        //                vialsBis.add(new Vial(upVial.getId() ,upVial.getShotNumber(), upVial.getVaccine(), slot));
                                        //            }
                                        //            else {
                                        //                vialsBis.add(new Vial(upVial.getId() ,upVial.getShotNumber(), upVial.getVaccine(), slot));
                                        //            }
//
                                        //        }
                                        //        Vial.insert(vialsBis);
                                        //        onReturn();
                                        //    }
                                        break;
                                    } else if (vial.getVaccine().getName().equals(vaccinStr)) { // Slot vide
                                        for (Vial vial2 : vials) {
                                            if (vial2.getSlot() != null && vial2.getSlot().getId().equals(slot1.getId())) {
                                                vial2.setSlot(null);
                                                vial2.removeSlot(vial.getVaccine().getId(), ele -> {
                                                });
                                            }
                                        }
                                        Log.d("good", "done");
                                        Intent intent = new Intent(this, DashBoardActivity.class);
                                        intent.putExtra("vaccines", vaccines);
                                        intent.putExtra("slots", slots);
                                        intent.putExtra("vials", vials);
                                        startActivity(intent);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    } else {

                    }

                }
            } else {
                Toast.makeText(this, "Wrong !", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v == bt_retour){
            onReturn();
        }
    }


    private void onReturn() {
        Intent intent = new Intent(this, DashBoardActivity.class);
        intent.putExtra("vaccines", vaccines);
        intent.putExtra("slots", slots);
        intent.putExtra("vials", vials);
        startActivity(intent);
    }

}

