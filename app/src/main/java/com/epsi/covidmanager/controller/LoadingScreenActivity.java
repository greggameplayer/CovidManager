package com.epsi.covidmanager.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.epsi.covidmanager.R;

public class LoadingScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);
    }
}