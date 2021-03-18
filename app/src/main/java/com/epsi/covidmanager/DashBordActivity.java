package com.epsi.covidmanager;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

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
