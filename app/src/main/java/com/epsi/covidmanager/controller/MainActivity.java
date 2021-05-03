package com.epsi.covidmanager.controller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Slot;
import com.epsi.covidmanager.model.beans.Token;
import com.epsi.covidmanager.model.beans.User;
import com.epsi.covidmanager.model.webservice.APIService;
import com.epsi.covidmanager.model.webservice.RetrofitHttpUtilis;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btConnection;
    EditText etLogin, etPassword;
    Context context = this;
    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Logger.addLogAdapter(new AndroidLogAdapter());
        btConnection = findViewById(R.id.bt_connection);
        etLogin = findViewById(R.id.ed_login);
        etPassword = findViewById(R.id.ed_password);
        btConnection.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        APIService apiService = RetrofitHttpUtilis.getRetrofitInstance().create(APIService.class);
        apiService.getConnection(new User(etLogin.getText().toString(), etPassword.getText().toString())).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200){
                    Token.setToken("Bearer " + response.body().getToken());
                    Intent intent = new Intent(context, SlotDashboard.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(context, "les identifiants sont incorrectes.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.w("TAGI", t.getMessage());
            }
        });

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "It's impossible to go backward", Toast.LENGTH_SHORT).show();
        moveTaskToBack(false);
    }



}