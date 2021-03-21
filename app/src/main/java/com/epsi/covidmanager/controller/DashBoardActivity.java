package com.epsi.covidmanager.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Slot;
import com.epsi.covidmanager.view.SlotAdaptater;

import java.util.ArrayList;

public class DashBoardActivity extends AppCompatActivity implements SlotAdaptater.OnSlotListener {

    //Composoants graphiques
    private RecyclerView rv_card_slot;
    //Donnees
    private ArrayList<Slot> slots;
    //Outil
    private SlotAdaptater slotAdaptater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        rv_card_slot = findViewById(R.id.rv_card_slot);

        slots = new ArrayList<>();

        for(int i = 0 ; i<=50 ; i++){
            slots.add(new Slot());

        }

        slotAdaptater = new SlotAdaptater(slots, this);
        rv_card_slot.setLayoutManager(new LinearLayoutManager(this));
        rv_card_slot.setAdapter(slotAdaptater);


    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    public void onClick(Slot slot) {
        Intent intent = new Intent(this, DetailsSlot.class);
        intent.putExtra(DetailsSlot.SLOT_KEY, slot);
        Log.w("TAG", slot.getId()+"");
        startActivity(intent);
    }
}
