package com.epsi.covidmanager.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Slot;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.beans.Vial;
import com.epsi.covidmanager.model.webservice.APIService;
import com.epsi.covidmanager.model.webservice.RetrofitHttpUtilis;
import com.epsi.covidmanager.view.VaccineAdaptater;
import com.google.android.material.navigation.NavigationView;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VaccineDashboard extends AppCompatActivity implements VaccineAdaptater.OnVaccineListener {

    private RecyclerView rv_dashboard_vaccine;
    private TextView id_alert_vaccin_names;
    private LinearLayout ly_alert_vaccins_quantity;
    private DrawerLayout drawerLayout;

    private ArrayList<Vaccine> vaccines;
    private ArrayList<Vial> vials;

    private VaccineAdaptater vaccineAdaptater;
    private MenuItem itDisconnect;
    private MenuItem vaccineDashboard;
    private MenuItem slotDashboard;
    private NavigationView navigationView;

    private VaccineDashboard context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.vaccine_dashboard);
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


        loadVials();

        id_alert_vaccin_names = findViewById(R.id.id_alert_vaccin_names);
        ly_alert_vaccins_quantity = findViewById(R.id.ly_alert_vaccins_quantity);

        rv_dashboard_vaccine = findViewById(R.id.rv_dashboard_vaccine);


        navigationView = findViewById(R.id.navigation);

        itDisconnect = navigationView.getMenu().findItem(R.id.disconnect);

        Intent mainActivity = new Intent(this, MainActivity.class);
        itDisconnect.setIntent(mainActivity);

        vaccineDashboard = navigationView.getMenu().findItem(R.id.dashboardVaccines);
        vaccineDashboard.setChecked(true);

        slotDashboard = navigationView.getMenu().findItem(R.id.dashboardSlots);
        Intent slotDashboardIntent = new Intent(this, DashBoardActivity.class);
        slotDashboardIntent.putExtra("vaccines", vaccines);

        slotDashboardIntent.putExtra("vials", vials);
        slotDashboard.setIntent(slotDashboardIntent);
    }

    public void loadVials(){

        APIService apiService = RetrofitHttpUtilis.getRetrofitInstance().create(APIService.class);

        apiService.getVials().enqueue(new Callback<List<Vial>>() {
            @Override
            public void onResponse(Call<List<Vial>> call, Response<List<Vial>> response) {
                vials = (ArrayList<Vial>) response.body();
                loadVaccines();
            }

            @Override
            public void onFailure(Call<List<Vial>> call, Throwable t) {
                Log.w("TAGI", t.getMessage());
            }
        });
    }

    public void loadVaccines(){

        APIService apiService = RetrofitHttpUtilis.getRetrofitInstance().create(APIService.class);

        apiService.getVaccines().enqueue(new Callback<List<Vaccine>>() {
            @Override
            public void onResponse(Call<List<Vaccine>> call, Response<List<Vaccine>> response) {
                vaccines = (ArrayList<Vaccine>) response.body();

                vaccineAdaptater = new VaccineAdaptater(vaccines, vials, context);
                rv_dashboard_vaccine.setLayoutManager(new LinearLayoutManager(context));
                rv_dashboard_vaccine.setAdapter(vaccineAdaptater);

                checkVaccinesQuantity();
            }

            @Override
            public void onFailure(Call<List<Vaccine>> call, Throwable t) {
                Log.w("TAGI", t.getMessage());
            }
        });
    }




    @Override
    public void onClick(Vaccine vaccine) {
        Intent intent = new Intent(this, DetailsVaccine.class);
        intent.putExtra("vaccine", vaccine);
        intent.putExtra("vaccines", vaccines);
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
}
