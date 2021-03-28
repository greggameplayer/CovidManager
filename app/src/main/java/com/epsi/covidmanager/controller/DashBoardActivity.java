package com.epsi.covidmanager.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import java.util.Objects;

public class DashBoardActivity extends AppCompatActivity implements SlotAdaptater.OnSlotListener, View.OnClickListener {
    //Composoants graphiques
    private RecyclerView rv_card_slot;
    private Button bt_add_slot;
    //Donnees
    private ArrayList<Slot> slots;
    private ArrayList<Vial> vials;
    private ArrayList<Vaccine> vaccines;
    private ArrayList<Slot> displayedSlots = new ArrayList<>();
    private ArrayList<Vial> displayedVials = new ArrayList<>();
    //Outil
    private SlotAdaptater slotAdaptater;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_opened, R.string.drawer_closed);

        // On définit notre ActionBarDrawerToggle comme listener.
        drawerLayout.addDrawerListener(drawerToggle);

        // On précise que l'on souhaite afficher la ressource graphique
        drawerToggle.setDrawerIndicatorEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // On synchronise.
        drawerToggle.syncState();

        vaccines = (ArrayList<Vaccine>) getIntent().getSerializableExtra("vaccines");
        slots = (ArrayList<Slot>) getIntent().getSerializableExtra("slots");
        vials = (ArrayList<Vial>) getIntent().getSerializableExtra("vials");

        for (Vial vial : vials) {
            if (vial.getSlot() != null) {
                displayedVials.add(vial);
            }
        }

        for (Slot slot : slots) {
            if (slot.isActive()) {
                displayedSlots.add(slot);
            }
        }

        Logger.addLogAdapter(new AndroidLogAdapter());
        Log.d("vaccines", vaccines.toString());
        Log.d("vials", vials.toString());
        Log.d("slots", slots.toString());
        rv_card_slot = findViewById(R.id.rv_card_slot);
        bt_add_slot = findViewById(R.id.bt_add_slot);

        bt_add_slot.setOnClickListener(this);

        slotAdaptater = new SlotAdaptater(displayedSlots, displayedVials, this);
        rv_card_slot.setLayoutManager(new LinearLayoutManager(this));
        rv_card_slot.setAdapter(slotAdaptater);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if(android.R.id.home == item.getItemId()) {
            if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
            else {
                drawerLayout.closeDrawers();
            }
        }

        return super.onOptionsItemSelected(item);
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
        Toast.makeText(this, "Implémenter le intent de onClick ajouter créneaux", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(Slot slot) {
        Intent intent = new Intent(this, DetailsSlot.class);
        intent.putExtra(DetailsSlot.SLOT_KEY, slot);
        intent.putExtra("vaccines", vaccines);
        intent.putExtra("slots", slots);
        intent.putExtra("vials", vials);
        startActivity(intent);
    }
}
