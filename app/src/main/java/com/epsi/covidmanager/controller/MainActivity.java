package com.epsi.covidmanager.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epsi.covidmanager.Mongo;
import com.epsi.covidmanager.R;
import com.epsi.covidmanager.controller.DashBoardActivity;

import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.sync.SyncConfiguration;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btConnection;
    EditText etLogin, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btConnection = findViewById(R.id.bt_connection);
        etLogin = findViewById(R.id.ed_login);
        etPassword = findViewById(R.id.ed_password);
        btConnection.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Realm.init(this);
                Mongo mongo = Mongo.getInstance("covidmanager-wweml");
                AtomicReference<User> user = mongo.getUser();
                App app = mongo.getApp();
                mongo.login(etLogin.getText().toString(), etPassword.getText().toString(), it -> {
                    if (it.isSuccess()) {
                        Log.v("AUTH", "Successfully authenticated.");
                        user.set(app.currentUser());
                        mongo.setClient(user.get().getMongoClient("mongodb-atlas"));
                        mongo.setDatabase(mongo.getClient().getDatabase("covidManager"));

                        SyncConfiguration config = new SyncConfiguration.Builder(user.get(), "CovidManager")
                                .allowQueriesOnUiThread(true)
                                .allowWritesOnUiThread(true)
                                .build();
                        Intent intent = new Intent(this, DashBoardActivity.class);
                        startActivity(intent);
                    } else {
                        Log.e("AUTH", it.getError().toString());
                        Toast.makeText(MainActivity.this, "This user doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_connection) {
            if(etLogin.getText().toString().isEmpty()) {
                Toast.makeText(MainActivity.this, "The email field shouldn't be empty", Toast.LENGTH_SHORT).show();
            } else if (etPassword.getText().toString().isEmpty()) {
                Toast.makeText(MainActivity.this, "The password field shouldn't be empty", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission_group.STORAGE, Manifest.permission.GLOBAL_SEARCH},
                        1);
            }
        }
    }
}