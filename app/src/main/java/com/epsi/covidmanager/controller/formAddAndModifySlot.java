package com.epsi.covidmanager.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
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
import com.epsi.covidmanager.model.beans.Token;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.beans.Vial;
import com.epsi.covidmanager.model.webservice.APIService;
import com.epsi.covidmanager.model.webservice.RetrofitHttpUtilis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class formAddAndModifySlot extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner spinnerVaccin;
    private EditText heureDebut, heureFin, nbDose;
    private Button bt_valider, bt_retour;
    private String vaccin;
    private ArrayList<Vial> vials;
    private ArrayList<Vaccine> vaccines;
    private AwesomeValidation heureValidation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
    private String oldVaccin;
    private ArrayList<Vial> vialsByVaccine = new ArrayList<>();


    private Slot slot;

    public static final long HOUR = 3600*1000; // in milli-seconds.

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_slot);

        vaccines = (ArrayList<Vaccine>) getIntent().getSerializableExtra("vaccines");
        vials = (ArrayList<Vial>) getIntent().getSerializableExtra("vials");


        slot = (Slot) getIntent().getSerializableExtra("slot");


        ArrayList<String> tabNameVaccines = new ArrayList<>();
        if (vials != null) {
            for (Vaccine vaccine : vaccines) {
                for (Vial vial : vials) {
                    if (vial.getVaccine().getName().equals(vaccine.getName())) {
                        tabNameVaccines.add(vaccine.getName());
                        break;
                    }
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


        int position = 0;
        if (slot != null) {
            for (Vial vial : vials) {
                if (vial.getSlot() != null && vial.getSlot().getId().equals(slot.getId())) {
                    position = adapter.getPosition(vial.getVaccine().getName());
                    break;
                }
            }
            spinnerVaccin.setSelection(position, true);
            nbDose.setText(Integer.toString(slot.getNbInitialPlaces()));
            heureDebut.setText(slot.getGoodFormatStartTime());
            heureFin.setText(slot.getGoodFormatEndTime());
        }

        bt_valider.setOnClickListener(this);
        bt_retour.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        if (v == bt_valider) {
            if (heureValidation.validate()) {
                getGoodVaccine();
            }

        } else if (v == bt_retour) {
            onReturn();
        }
    }

    private void onReturn() {
        Intent intent = new Intent(this, SlotDashboard.class);
        startActivity(intent);
    }

    public void getGoodVaccine() {
        for (Vaccine vaccine : vaccines) {
            if (vaccine.getName().equals(vaccin)) {

                getVialsByIdVaccine(vaccine.getId());
            }
        }
    }


    private void getVialsByIdVaccine(int IdVaccine) {
        APIService apiService = RetrofitHttpUtilis.getRetrofitInstance().create(APIService.class);
        apiService.getVialsByVaccineId(Token.getToken(), IdVaccine).enqueue(new Callback<List<Vial>>() {
            @Override
            public void onResponse(Call<List<Vial>> call, Response<List<Vial>> response) {
                vialsByVaccine = (ArrayList<Vial>) response.body();

                if (slot != null) {
                    deleteSlot();
                } else {
                    checkQuantity();
                }

            }

            @Override
            public void onFailure(Call<List<Vial>> call, Throwable t) {
                Log.w("TAGI", t.getMessage());
            }
        });
    }

    private void checkQuantity() {
        int i = 0;
        for (Vial vial : vialsByVaccine) {
            if(vial.getSlot()==null){
                i += vial.getShotNumber();
            }
            else {
                vialsByVaccine.remove(vial);
            }
        }
        if (i >= Integer.parseInt(nbDose.getText().toString())) {
            addSlot(Integer.parseInt(nbDose.getText().toString()));
        } else {
            Toast.makeText(this, "Il n'y a pas assez de vaccins disponible !", Toast.LENGTH_SHORT).show();
        }

    }

    private void addSlot(int nbDose) {
        APIService apiService = RetrofitHttpUtilis.getRetrofitInstance().create(APIService.class);
        Date startTime = null;
        Date endTime = null;

        try {
            //startTime = new Date(new Date(String.valueOf(heureDebut.getText())).getTime() + 2 * HOUR);
            //endTime = new Date(new Date(String.valueOf(heureFin.getText())).getTime() + 2 * HOUR);
            startTime = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(String.valueOf(heureDebut.getText()));
            endTime = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(String.valueOf(heureFin.getText()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (endTime.getTime() > startTime.getTime()) {
            apiService.createSlot(Token.getToken(), new Slot(startTime, endTime, 0, nbDose)).enqueue(new Callback<Slot>() {
                @Override
                public void onResponse(Call<Slot> call, Response<Slot> response) {
                    attributeVialsToSlot(nbDose, response.body());
                }

                @Override
                public void onFailure(Call<Slot> call, Throwable t) {
                    Log.w("TAGI1", t.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "La date de fin ne peut pas être avant la date de début", Toast.LENGTH_SHORT).show();
        }
    }

    private void attributeVialsToSlot(int nbDose, Slot newSlot) {
        int i = 0;
        for (Vial vial : vialsByVaccine) {
            Log.w("TAGI", String.valueOf(newSlot.getId()));
            if (i < nbDose) {
                APIService apiService = RetrofitHttpUtilis.getRetrofitInstance().create(APIService.class);
                vial.setSlot(newSlot);
                vial.setIdSlot(newSlot.getId());
                apiService.updateVial(Token.getToken(), vial.getId(), vial).enqueue(new Callback<Vial>() {
                    @Override
                    public void onResponse(Call<Vial> call, Response<Vial> response) {
                        onReturn();
                    }

                    @Override
                    public void onFailure(Call<Vial> call, Throwable t) {
                        Log.w("TAGI2", t.getMessage());
                    }
                });
                i += vial.getShotNumber();
            }
        }
    }

    private void deleteSlot() {
        if (checkDates() && checkQuantityToDelete()) {
            APIService apiService = RetrofitHttpUtilis.getRetrofitInstance().create(APIService.class);
            apiService.deleteSlot(Token.getToken(), slot.getId()).enqueue(new Callback<Slot>() {
                @Override
                public void onResponse(Call<Slot> call, Response<Slot> response) {
                    addSlot(Integer.parseInt(nbDose.getText().toString()));
                }

                @Override
                public void onFailure(Call<Slot> call, Throwable t) {
                    Log.w("TAGI3", t.getMessage());
                }
            });

        } else {
            Toast.makeText(this, "Il n'y a pas assez de doses disponible pour le vaccin " + vaccin + ".", Toast.LENGTH_SHORT).show();
        }

    }

    public Boolean checkDates() {
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(String.valueOf(heureDebut.getText()));
            endTime = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(String.valueOf(heureFin.getText()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (endTime.getTime() > startTime.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean checkQuantityToDelete() {
        int i = 0;
        for (Vial vial : vialsByVaccine) {
            i += vial.getShotNumber();
        }
        if (oldVaccin.equals(vaccin)){
            i += slot.getNbInitialPlaces();
        }


        if (i >= Integer.parseInt(nbDose.getText().toString())) {
            return true;
        } else {
            return false;
        }

    }
}

