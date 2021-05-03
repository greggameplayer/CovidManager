package com.epsi.covidmanager.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Token;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.beans.Vial;
import com.epsi.covidmanager.model.webservice.APIService;
import com.epsi.covidmanager.model.webservice.RetrofitHttpUtilis;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class formAddVaccine extends AppCompatActivity implements View.OnClickListener {


    private Vaccine vaccine;
    private int nbVial, nbDose;

    private ArrayList<Vaccine> vaccines;
    private ArrayList<Vial> vials;


    private TextView tv_form_vaccine_name;
    private EditText et_form_vaccine_nbVial, et_form_vaccine_nbDose;
    private Button bt_form_vaccine_add, bt_form_vaccine_return;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_vaccine);

        vaccine = (Vaccine) getIntent().getExtras().getSerializable("vaccine");

        vaccines = (ArrayList<Vaccine>) getIntent().getSerializableExtra("vaccines");
        vials = (ArrayList<Vial>) getIntent().getSerializableExtra("vials");

        et_form_vaccine_nbVial = findViewById(R.id.et_form_vaccine_nbVial);
        et_form_vaccine_nbDose = findViewById(R.id.et_form_vaccine_nbDose);
        tv_form_vaccine_name = findViewById(R.id.tv_form_vaccine_name);
        tv_form_vaccine_name.setText(vaccine.getName());

        bt_form_vaccine_add = findViewById(R.id.bt_form_vaccine_add);
        bt_form_vaccine_return = findViewById(R.id.bt_form_vaccine_return);

        bt_form_vaccine_return.setOnClickListener(this);
        bt_form_vaccine_add.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == bt_form_vaccine_add.getId()){
            if(TextUtils.isEmpty(et_form_vaccine_nbDose.getText().toString()) || TextUtils.isEmpty(et_form_vaccine_nbVial.getText().toString()) ){
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
            else{
                onAdd();
            }
        }else{
            onReturn();
        }

    }

    private void onReturn(){
        Intent intent = new Intent(this, DetailsVaccine.class);
        intent.putExtra("vaccine", vaccine);
        intent.putExtra("vaccines", vaccines);
        intent.putExtra("vials", vials);
        startActivity(intent);
    }

    private void onReturnDashboard(){
        Intent intent = new Intent(this, VaccineDashboard.class);
        startActivity(intent);
    }
    private void onAdd(){

        ArrayList<Vial> vialsBis = new ArrayList<>();

        nbVial = Integer.parseInt(et_form_vaccine_nbVial.getText().toString());
        nbDose = Integer.parseInt(et_form_vaccine_nbDose.getText().toString());


        for(int i = 0 ; i < nbVial ; i++){

            Vial vial = new Vial(nbDose, vaccine);
            Log.w("TAGI", vial.toString());


            APIService apiService = RetrofitHttpUtilis.getRetrofitInstance().create(APIService.class);

            int j = i;
            apiService.createVial(Token.getToken(), vial).enqueue(new Callback<Vial>() {
                @Override
                public void onResponse(Call<Vial> call, Response<Vial> response) {
                    //Ajouter la vérification du code 200

                    if(j==nbVial){
                        Log.w("TAGI", "end");
                        onReturnDashboard();
                    }
                }

                @Override
                public void onFailure(Call<Vial> call, Throwable t) {
                    Log.w("TAGI", t.getMessage());
                }
            });

            onReturnDashboard();
        }






    }
}
