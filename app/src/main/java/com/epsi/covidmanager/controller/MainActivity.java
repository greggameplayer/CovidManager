package com.epsi.covidmanager.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epsi.covidmanager.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText ed_login, ed_password;
    Button bt_connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_login = findViewById(R.id.ed_login);
        ed_password =  findViewById(R.id.ed_password);
        bt_connection =  findViewById(R.id.bt_connection);

        bt_connection.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String login = ed_login.getText().toString();
        String password = ed_password.getText().toString();

        BDD(login, password);
    }

    public void BDD(String login, String password){
        //TODO créer les requetes de vérification de login et de password
        boolean verifLogin;
        boolean verifPassword = false;

        //requete login
        verifLogin = (login.equals("login"));
        if (verifLogin){
            //requete password en fonction du login
            verifPassword = (password.equals("password"));
        }


        if(verifLogin && verifPassword){
            Intent intent = new Intent(this, DashBoardActivity.class);
            startActivity(intent);
        }
        else if(verifLogin && !verifPassword) {
            Toast.makeText(this, "le mot de passe entré ne correspond pas", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "l'indentifiant entré ne correspond pas", Toast.LENGTH_LONG).show();
        }
    }
}