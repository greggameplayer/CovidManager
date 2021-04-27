package com.epsi.covidmanager.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;

public class formAddAndModifySlot extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner spinnerVaccin;
    private EditText heureDebut, heureFin, nbDose;
    private Button bt_valider, bt_retour;
    private String vaccin;
    private ArrayList<Slot> slots;
    private ArrayList<Vial> vials;
    private ArrayList<Vaccine> vaccines;
    private AwesomeValidation heureValidation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
    private SimpleDateFormat dateFormat;
    private String oldVaccin;

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
                if (vial.getSlot() != null && vial.getSlot().getId() == (slot.getId())) {
                    position = adapter.getPosition(vial.getVaccine().getName());
                    break;
                }
            }
            spinnerVaccin.setSelection(position, true);
            nbDose.setText(Integer.toString(slot.getNbInitialPlaces()));
            heureDebut.setText(dateFormat.format(slot.getStartTime()));
            heureFin.setText(dateFormat.format(slot.getEndTime()));
        }

        //bt_valider.setOnClickListener(this);
        //bt_retour.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (oldVaccin == null) {
            oldVaccin = (String) parent.getItemAtPosition(position);
        }
        vaccin = (String) parent.getItemAtPosition(position);
        Log.w("vaccin", vaccin);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //@Override
    //public void onClick(View v) {
//
    //    if (v == bt_valider) {
    //        if (heureValidation.validate()) {
    //            boolean isPossible = true;
    //            int totalDoses = 0;
    //            int nbDoses = 0;
    //            final int[] incrementDoses = {0};
//
    //            String vaccinStr = vaccin;
    //            Vaccine vaccinObj = null;
    //            for (Vaccine vaccine : vaccines) {
    //                if (vaccine.getName().equals(vaccinStr)) {
    //                    vaccinObj = vaccine;
    //                    break;
    //                }
    //            }
    //            int nombreDoseStr = Integer.parseInt(nbDose.getText().toString());
    //            Date heureDebutStr = null;
    //            Date heureFinStr = null;
//
    //            try {
    //                dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
    //                heureDebutStr = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(String.valueOf(heureDebut.getText()));
    //                heureFinStr = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(String.valueOf(heureFin.getText()));
    //            } catch (ParseException e) {
    //                e.printStackTrace();
    //            }
//
//
    //            nbDoses = nombreDoseStr;
    //            for (Vial vial : vials) {
    //                if (vial.getVaccine().getName().equals(vaccinStr) && vial.getSlot() == null) {
    //                    totalDoses += vial.getShotNumber();
    //                    if (nbDoses >= vial.getShotNumber()) {
    //                        nbDoses -= vial.getShotNumber();
    //                        incrementDoses[0] += vial.getShotNumber();
    //                    }
    //                }
//
    //                for (Vial vial1 : vials) {
    //                    if (vial1.getSlot() != null && ((vial1.getSlot().getStartTime().after(heureDebutStr) && vial1.getSlot().getEndTime().before(heureDebutStr)) || (vial1.getSlot().getStartTime().after(heureFinStr) && vial1.getSlot().getEndTime().before(heureFinStr)))) {
    //                        Toast.makeText(this, "Les différents créneaux ne peuvent pas se superposer ( créneaux existant concerné :" + vial.getSlot().getStartTime() + " - " + vial.getSlot().getEndTime() + ")", Toast.LENGTH_LONG).show();
    //                        isPossible = false;
    //                        break;
    //                    }
    //                }
    //            }
//
    //            //TODO: revoir vérif par rapport à l'heure actuelle
    //            //if(new Date().after(heureDebutStr) || new Date().after(heureFinStr)){
    //            //    Log.w("test", String.valueOf(new Date()));
    //            //    Toast.makeText(this, "l'une des dates rentré est avant la date actuelle", Toast.LENGTH_LONG).show();
    //            //    isPossible = false;
    //            //    break;
    //            //}
    //            if (heureDebutStr.after(heureFinStr) && isPossible) {
    //                Toast.makeText(this, "La date de fin ne peut pas être avant la date de début", Toast.LENGTH_LONG).show();
    //                isPossible = false;
    //            }
//
    //            if (nombreDoseStr > totalDoses && isPossible) {
    //                Toast.makeText(this, "Le nombre de doses entrées est supérieur au stock actuel (" + totalDoses + ")", Toast.LENGTH_LONG).show();
    //                isPossible = false;
    //            }
    //            if (nombreDoseStr != incrementDoses[0] && isPossible) {
    //                Toast.makeText(this, "Un nombre total de " + nombreDoseStr + " doses n'est pas atteignable de façon exacte ( trop plein de :" + Math.abs(incrementDoses[0] - nombreDoseStr) + ")", Toast.LENGTH_LONG).show();
    //                isPossible = false;
    //            }
    //            if (slot != null && nombreDoseStr < slot.getNbReservedPlaces() && isPossible) {
    //                Toast.makeText(this, "Un nombre de " + nombreDoseStr + " doses est inférieur au nombre de place réservé (" + slot.getNbReservedPlaces() + ")", Toast.LENGTH_LONG).show();
    //                isPossible = false;
    //            }
    //            if (isPossible) {
    //                if (slot != null) {
    //                    for (Slot slot1 : slots) {
    //                        if (slot1.getId()(slot.getId())) {
    //                            slot1.setEndTime(heureFinStr);
    //                            slot1.setStartTime(heureDebutStr);
    //                            slot1.setNbInitialPlaces(nombreDoseStr);
    //                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Slot");
//
    //                            // Retrieve the object by id
    //                            Date finalHeureDebutStr = heureDebutStr;
    //                            Date finalHeureFinStr = heureFinStr;
    //                            query.getInBackground(slot1.getId(), (object, e) -> {
    //                                if (e == null) {
    //                                    //Object was successfully retrieved
    //                                    // Update the fields we want to
    //                                    slot1.setStartTime(finalHeureDebutStr);
    //                                    slot1.setEndTime(finalHeureFinStr);
    //                                    slot1.setNbInitialPlaces(nombreDoseStr);
    //                                    object.put("startTime", slot1.getStartTime());
    //                                    object.put("endTime", slot1.getEndTime());
    //                                    object.put("nbInitialPlaces", slot1.getNbInitialPlaces());
//
    //                                    //All other fields will remain the same
    //                                    object.saveInBackground((ell) -> {
//
    //                                    });
//
    //                                } else {
    //                                    // something went wrong
    //                                    Log.d("ERRORUPDATE1", e.getMessage());
    //                                }
    //                            });
    //                            AtomicBoolean deleteFinish = new AtomicBoolean(false);
    //                            AtomicBoolean firstTime = new AtomicBoolean(false);
    //                            for (Vial vial : vials) {
    //                                Object lockObject = new Object();
    //                                Thread updateThread = new Thread() {
    //                                    @Override
    //                                    public void run() {
    //                                        Log.d("WAIT", "2");
    //                                        if (vial.getVaccine().getName().equals(vaccinStr) && incrementDoses[0] >= vial.getShotNumber()) {
    //                                            incrementDoses[0] -= vial.getShotNumber();
    //                                            vial.setSlot(slot1);
    //                                            ParseQuery<ParseObject> queryVial = ParseQuery.getQuery("Vial");
    //                                            queryVial.getInBackground(vial.getId(), (object1, a) -> {
    //                                                synchronized (lockObject) {
    //                                                    if (a == null) {
    //                                                        //Object was successfully retrieved
    //                                                        // Update the fields we want to
    //                                                        object1.put("slotId", vial.getSlot().getId());
//
    //                                                        //All other fields will remain the same
    //                                                        object1.saveInBackground((zd) -> {
//
    //                                                        });
//
    //                                                    } else {
    //                                                        // something went wrong
    //                                                        Log.d("ERRORUPDATE", a.getMessage());
    //                                                    }
    //                                                }
    //                                            });
    //                                        }
    //                                    }
    //                                };
    //                                Thread removeThread = new Thread() {
    //                                    @Override
    //                                    public void run() {
    //                                        if (vial.getVaccine().getName().equals(oldVaccin)) {
    //                                            Log.w("slot1.getId()", slot1.getId());
    //                                            if (vial.getSlot() != null && vial.getSlot().getId().equals(slot1.getId())) {
    //                                                Log.w("vialTEST", vial.getVaccine().getName());
    //                                                vial.setSlot(null);
    //                                                ParseQuery<ParseObject> queryVial = ParseQuery.getQuery("Vial");
    //                                                queryVial.getInBackground(vial.getId(), (object, e) -> {
    //                                                    synchronized (lockObject) {
    //                                                        if (e == null) {
    //                                                            //Object was successfully retrieved
    //                                                            // Update the fields we want to
    //                                                            object.remove("slotId");
//
    //                                                            //All other fields will remain the same
    //                                                            object.saveInBackground((el) -> {
    //                                                                Log.d("WAIT", "1");
    //                                                                updateThread.start();
    //                                                            });
//
    //                                                        } else {
    //                                                            // something went wrong
    //                                                            Log.d("ERRORUPDATE2", e.getMessage());
    //                                                        }
    //                                                    }
    //                                                });
    //                                            }
    //                                        } else {
    //                                            synchronized (lockObject) {
    //                                                Log.d("WAIT", "1");
    //                                                updateThread.start();
    //                                            }
    //                                        }
    //                                    }
    //                                };
//
    //                                removeThread.start();
    //                            }
    //                        }
    //                    }
    //                    onReturn();
    //                } else {
    //                    String newID = Integer.toString((int) (Math.random() * ((999999999) + 1)));
    //                    Object insertLockObject = new Object();
    //                    Slot newSlot = new Slot(newID, heureDebutStr, heureFinStr, 0, nombreDoseStr);
    //                    Context that = this;
//
    //                    Thread insertUpdateThread = new Thread() {
    //                        @Override
    //                        public void run() {
    //                            synchronized (insertLockObject) {
    //                                for (Vial vial : vials) {
    //                                    if (vial.getSlot() == null && vial.getVaccine().getName().equals(vaccinStr) && incrementDoses[0] >= vial.getShotNumber()) {
    //                                        vial.setSlot(newSlot);
    //                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Vial");
//
    //                                        // Retrieve the object by id
    //                                        query.getInBackground(vial.getId(), (object, e) -> {
    //                                            if (e == null) {
    //                                                //Object was successfully retrieved
    //                                                // Update the fields we want to
    //                                                vial.setSlot(newSlot);
    //                                                object.put("slotId", vial.getSlot().getId());
    //                                                Log.w("insert", "insertSlot in Vials");
//
    //                                                //All other fields will remain the same
    //                                                object.saveInBackground((l) -> {
    //                                                    incrementDoses[0] -= vial.getShotNumber();
    //                                                });
    //                                            } else {
    //                                                // something went wrong
    //                                                Log.d("ERRORUPDATE", e.getMessage());
    //                                            }
    //                                        });
    //                                    }
    //                                }
    //                                insertLockObject.notify();
    //                            }
    //                        }
    //                    };
    //                    Thread getThread = new Thread() {
    //                        @Override
    //                        public void run() {
    //                            ParseQuery<ParseObject> queryFind = ParseQuery.getQuery("Slot");
    //                            queryFind.whereEqualTo("startTime", newSlot.getStartTime()).whereEqualTo("endTime", newSlot.getEndTime());
    //                            try {
    //                                List<ParseObject> object = queryFind.find();
    //                                newSlot.setId(object.get(0).getObjectId());
    //                                slots.add(newSlot);
    //                                insertUpdateThread.start();
    //                            } catch (com.parse.ParseException e) {
    //                                e.printStackTrace();
    //                            }
    //                        }
    //                    };
    //                    Thread insertThread = new Thread() {
    //                        @Override
    //                        public void run() {
    //                            ParseObject entity = new ParseObject("Slot");
//
    //                            entity.put("startTime", newSlot.getStartTime());
    //                            entity.put("endTime", newSlot.getEndTime());
    //                            entity.put("nbReservedPlaces", newSlot.getNbReservedPlaces());
    //                            entity.put("nbInitialPlaces", newSlot.getNbInitialPlaces());
    //                            entity.saveInBackground((e -> {
    //                                synchronized (insertLockObject) {
    //                                    Log.w("insert", "insertSlot");
    //                                    getThread.start();
    //                                    try {
    //                                        insertLockObject.wait();
    //                                    } catch (InterruptedException interruptedException) {
    //                                        interruptedException.printStackTrace();
    //                                    }
//
    //                                    onReturn();
    //                                }
    //                            }));
    //                        }
    //                    };
    //                    insertThread.start();
    //                }
    //            }
    //        } else {
    //            Toast.makeText(this, "Wrong !", Toast.LENGTH_SHORT).show();
    //        }
    //    } else if (v == bt_retour) {
    //        onReturn();
    //    }
//
    //}


    private void onReturn() {
        Intent intent = new Intent(this, DashBoardActivity.class);
        intent.putExtra("vaccines", vaccines);
        intent.putExtra("slots", slots);
        intent.putExtra("vials", vials);
        startActivity(intent);
    }

}

