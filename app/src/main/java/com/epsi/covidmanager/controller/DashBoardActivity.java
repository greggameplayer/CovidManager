package com.epsi.covidmanager.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.epsi.covidmanager.model.beans.Slot;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.beans.Vial;
import com.epsi.covidmanager.R;
import com.epsi.covidmanager.view.SlotAdaptater;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

public class DashBoardActivity extends AppCompatActivity implements SlotAdaptater.OnSlotListener, View.OnClickListener {
    //Composoants graphiques
    private RecyclerView rv_card_slot;
    private Button bt_add_slot;
    //Donnees
    private ArrayList<Slot> slots = new ArrayList<>();
    private ArrayList<Vial> vials = new ArrayList<>();
    private ArrayList<Vaccine> vaccines = new ArrayList<>();
    //Outil
    private SlotAdaptater slotAdaptater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vaccines = Vaccine.findAll(this);
        if (vaccines != null) {
            vials = Vial.findAll(this, vaccines);
            if (vials != null) {
                slots = Slot.findAll(this, vials);
            }
        }
        setContentView(R.layout.dashboard);
        Logger.addLogAdapter(new AndroidLogAdapter());
        Log.d("vaccines", vaccines.toString());
        Log.d("vials", vials.toString());
        Log.d("slots", slots.toString());
        rv_card_slot = findViewById(R.id.rv_card_slot);
        bt_add_slot = findViewById(R.id.bt_add_slot);

        bt_add_slot.setOnClickListener(this);

        slotAdaptater = new SlotAdaptater(slots, this);
        rv_card_slot.setLayoutManager(new LinearLayoutManager(this));
        rv_card_slot.setAdapter(slotAdaptater);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "It's impossible to go backward", Toast.LENGTH_SHORT).show();
        moveTaskToBack(false);
    }

    @Override
    public void onClick(View v) {
        //Intent intent = new Intent(this, .....class);
        //startActivity(intent);
        Toast.makeText(this, "Implémenter le intent de onClick", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(Slot slot) {
        Intent intent = new Intent(this, DetailsSlot.class);
        intent.putExtra(DetailsSlot.SLOT_KEY, slot);
        startActivity(intent);
    }
}
