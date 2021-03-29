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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class formAddAndModifySlot extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner spinnerVaccin;
    private EditText heureDebut, heureFin, nbDose;
    private Button bt_valider;
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

        if (heureValidation.validate()) {
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
                if (slot != null) {
                    for (Slot slot1 : slots) {
                        if (slot1.getId().equals(slot.getId())) {
                            for (Vial vial : vials) {
                                if (vial.getVaccine().getId().equals(vaccinStr) && vial.getSlot().equals(slot1)) {
                                    slot1.updateAll(heureDebutStr, heureFinStr, nombreDoseStr, (e) -> {
                                        synchronized (lockObject) {
                                            AtomicInteger nb = new AtomicInteger();
                                            Vial.removeSlot(vials, vial.getVaccine().getId(), lockObject, nb);
                                            while (nb.get() == vials.size()) {
                                                try {
                                                    lockObject.wait();
                                                } catch (InterruptedException interruptedException) {
                                                    interruptedException.printStackTrace();
                                                }
                                            }
                                        }
                                        //TODO: update vials
                                    });
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


    private void onReturn() {
        Intent intent = new Intent(this, DashBoardActivity.class);
        intent.putExtra("vaccines", vaccines);
        intent.putExtra("slots", slots);
        intent.putExtra("vials", vials);
        startActivity(intent);
    }

}

