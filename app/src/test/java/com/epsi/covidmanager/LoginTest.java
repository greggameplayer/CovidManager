package com.epsi.covidmanager;

import android.util.Log;

import com.epsi.covidmanager.model.beans.Token;
import com.epsi.covidmanager.model.beans.User;
import com.epsi.covidmanager.model.webservice.APIService;
import com.epsi.covidmanager.model.webservice.RetrofitHttpUtilis;

import org.junit.Test;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static org.junit.Assert.*;

public class LoginTest {

    private APIService apiService;

    public LoginTest() {
        apiService = RetrofitHttpUtilis.getRetrofitInstance().create(APIService.class);
    }

    @Test
    public void loginSuccess() {
        apiService.getConnection(new User("visiteur@visiteur.fr", "visiteur")).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                assertFalse("Erreur : " + t.getMessage(), false);
            }
        });
    }

    @Test
    public void loginFailed() {
        apiService.getConnection(new User("visiteur@fr.fr", "visiteur")).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                assertNotEquals(200, response.code());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                assertFalse("Erreur : " + t.getMessage(), false);
            }
        });
    }
}
