package com.epsi.covidmanager.controller;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Slot;

public class DetailsSlot extends AppCompatActivity {

    public final static String SLOT_KEY = "SLOT_KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_slot);

        Slot slot = (Slot) getIntent().getExtras().getSerializable(SLOT_KEY);

    }
}