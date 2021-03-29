package com.epsi.covidmanager.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Secretary;
import com.epsi.covidmanager.model.beans.Slot;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.beans.Vial;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btConnection;
    EditText etLogin, etPassword;

    private ArrayList<Slot> slots = new ArrayList<>();
    private ArrayList<Vial> vials = new ArrayList<>();
    private ArrayList<Vaccine> vaccines = new ArrayList<>();
    private final Object lockObject = new Object();

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
        Context that = this;
        AtomicReference<List<ParseObject>> futureVials = new AtomicReference<>();
        AtomicInteger numberResults = new AtomicInteger(0);
        Thread thread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Vial");
                query.setLimit(1000);
                query.findInBackground((objects, e) -> {
                    synchronized (lockObject) {
                        if (e == null) {
                            futureVials.set(objects);
                            try {
                                numberResults.set(query.count());
                            } catch (ParseException parseException) {
                                parseException.printStackTrace();
                            }
                            lockObject.notify();
                        } else {
                            Log.e("ERROR", e.getMessage());
                        }
                    }
                });
                Looper.loop();
            }
        };
        Thread mainThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                synchronized (lockObject) {
                    if (v.getId() == R.id.bt_connection) {
                        if (etLogin.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, "The email field shouldn't be empty", Toast.LENGTH_SHORT).show();
                        } else if (etPassword.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, "The password field shouldn't be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent loadingScreen = new Intent(that, LoadingScreenActivity.class);
                            startActivity(loadingScreen);
                            Secretary secretary = Secretary.login(etLogin.getText().toString(), etPassword.getText().toString(), that);
                            if (secretary != null) {
                                vaccines = Vaccine.findAll(that);
                                if (vaccines != null) {
                                    slots = Slot.findAll(that, vials);
                                    if (slots != null) {
                                        thread.start();
                                        while (futureVials.get() == null || futureVials.get().size() != numberResults.get()) {
                                            try {
                                                lockObject.wait();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        Log.d("VIALS", Integer.toString(futureVials.get().size()));
                                        for (ParseObject result : futureVials.get()) {
                                            for (Vaccine vaccine : vaccines) {
                                                if (vaccine.getId().equals(result.getString("vaccineId"))) {
                                                    for (Slot slot : slots) {
                                                        if (result.getString("slotId") == null) {
                                                            vials.add(new Vial(result.getObjectId(), result.getInt("shotNumber"), vaccine));
                                                            break;
                                                        } else if (slot.getId().equals(result.getString("slotId"))) {
                                                            vials.add(new Vial(result.getObjectId(), result.getInt("shotNumber"), vaccine, slot));
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Intent intent = new Intent(that, DashBoardActivity.class);
                                intent.putExtra("secretary", secretary);
                                intent.putExtra("vaccines", vaccines);
                                intent.putExtra("slots", slots);
                                intent.putExtra("vials", vials);
                                startActivity(intent);
                            } else {
                                Intent mainActivity = new Intent(that, MainActivity.class);
                                startActivity(mainActivity);
                            }
                        }
                    }
                }
                Looper.loop();
            }
        };
        mainThread.start();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "It's impossible to go backward", Toast.LENGTH_SHORT).show();
        moveTaskToBack(false);
    }


}