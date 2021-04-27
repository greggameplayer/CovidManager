package com.epsi.covidmanager.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.epsi.covidmanager.model.beans.Slot;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.beans.Vial;
import com.epsi.covidmanager.R;
import com.epsi.covidmanager.view.SlotAdaptater;
import com.google.android.material.navigation.NavigationView;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Objects;

public class DashBoardActivity extends AppCompatActivity implements SlotAdaptater.OnSlotListener, View.OnClickListener {
    //Composoants graphiques
    private RecyclerView rv_card_slot;
    private Button bt_add_slot;
    private MenuItem itDisconnect;
    private MenuItem vaccineDashboard;
    private MenuItem slotDashboard;
    private NavigationView navigationView;
    //Donnees
    private ArrayList<Slot> slots;
    private ArrayList<Vial> vials;
    private ArrayList<Vaccine> vaccines;
    private ArrayList<Integer> IDdisplayedSlots = new ArrayList<>();
    private ArrayList<Slot> displayedSlots = new ArrayList<>();
    private ArrayList<Vial> displayedVials = new ArrayList<>();
    //Outil
    private SlotAdaptater slotAdaptater;
    private DrawerLayout drawerLayout;
    private LinearLayout ly_alert_vaccins_quantity;
    private TextView id_alert_vaccin_names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.design_default_color_background, getTheme()));

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
                for (Slot slot : slots) {
                    if (vial.getSlot().getId() == slot.getId()) {
                        IDdisplayedSlots.add(vial.getSlot().getId());
                        break;
                    }
                }
            }
        }

        for (Slot slot : slots){
            for (Integer IDslot: IDdisplayedSlots) {
                if(IDslot == slot.getId()){
                    displayedSlots.add(slot);
                    break;
                }
            }
        }


        Logger.addLogAdapter(new AndroidLogAdapter());
        Log.d("vaccines", vaccines.toString());
        Log.d("vials", vials.toString());
        Log.d("slots", slots.toString());
        rv_card_slot = findViewById(R.id.rv_card_slot);
        bt_add_slot = findViewById(R.id.bt_add_slot);
        ly_alert_vaccins_quantity = findViewById(R.id.ly_alert_vaccins_quantity);
        id_alert_vaccin_names = findViewById(R.id.id_alert_vaccin_names);
        checkVaccinesQuantity();

        bt_add_slot.setOnClickListener(this);

        navigationView = findViewById(R.id.navigation);

        itDisconnect = navigationView.getMenu().findItem(R.id.disconnect);

        Intent mainActivity = new Intent(this, MainActivity.class);
        itDisconnect.setIntent(mainActivity);

        vaccineDashboard = navigationView.getMenu().findItem(R.id.dashboardVaccines);
        Intent vaccineDashboardIntent = new Intent(this, VaccineDashboard.class);
        vaccineDashboardIntent.putExtra("vaccines", vaccines);
        vaccineDashboardIntent.putExtra("slots", slots);
        vaccineDashboardIntent.putExtra("vials", vials);
        vaccineDashboard.setIntent(vaccineDashboardIntent);

        slotDashboard = navigationView.getMenu().findItem(R.id.dashboardSlots);

        slotDashboard.setChecked(true);

        slotAdaptater = new SlotAdaptater(displayedSlots, displayedVials, this);
        rv_card_slot.setLayoutManager(new LinearLayoutManager(this));
        rv_card_slot.setAdapter(slotAdaptater);
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                drawerLayout.openDrawer(Gravity.LEFT);
            } else {
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
        Intent intent = new Intent(this, formAddAndModifySlot.class);
        intent.putExtra("vaccines", vaccines);
        intent.putExtra("slots", slots);
        intent.putExtra("vials", vials);

        startActivity(intent);
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

    public void checkVaccinesQuantity(){
        StringBuilder value = new StringBuilder();
        for(Vaccine vaccine : vaccines){
            int nb = quantityRemainToAllow(vaccine);
            if(nb < 200){
                value.append(vaccine.getName().toUpperCase()).append(" \n");
            }
        }
        if(!value.toString().equals("")){
            ly_alert_vaccins_quantity.setVisibility(View.VISIBLE);
            id_alert_vaccin_names.setText(value.toString());
        }

    }

    public int quantityRemainToAllow(Vaccine vaccine){
        int nb = 0;
        for(Vial vial : vials){
            if(vial.getVaccine().getName().equals(vaccine.getName()) && vial.getSlot() == null){
                nb = nb+vial.getShotNumber();
            }
        }
        return nb;
    }
}
