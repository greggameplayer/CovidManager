package com.epsi.covidmanager.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Secretary;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.parse.Parse;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btConnection;
    EditText etLogin, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("udOrqMlAKIhiaf0efpCSTLMaheLnG5m3wVAqPTys")
                .clientKey("uZGtxAnHNKYcVTcWTsXmgCiB5KW2oGMLsPr7AvZp")
                .server("https://parseapi.back4app.com")
                .build()
        );

        setContentView(R.layout.activity_main);
        Logger.addLogAdapter(new AndroidLogAdapter());
        btConnection = findViewById(R.id.bt_connection);
        etLogin = findViewById(R.id.ed_login);
        etPassword = findViewById(R.id.ed_password);
        btConnection.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_connection) {
            if(etLogin.getText().toString().isEmpty()) {
                Toast.makeText(MainActivity.this, "The email field shouldn't be empty", Toast.LENGTH_SHORT).show();
            } else if (etPassword.getText().toString().isEmpty()) {
                Toast.makeText(MainActivity.this, "The password field shouldn't be empty", Toast.LENGTH_SHORT).show();
            } else {
                Secretary secretary = Secretary.login(etLogin.getText().toString(), etPassword.getText().toString(), this);
                if(secretary != null) {
                    Intent intent = new Intent(this, DashBoardActivity.class);
                    intent.putExtra("secretary", secretary);
                    startActivity(intent);
                }
            }
        }
    }
}