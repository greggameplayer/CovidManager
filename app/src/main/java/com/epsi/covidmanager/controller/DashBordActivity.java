package com.epsi.covidmanager.controller;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import com.epsi.covidmanager.R;

public class DashBordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashbord);

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}
