package com.epsi.covidmanager;

import android.util.Log;

import com.epsi.covidmanager.controller.DetailsSlot;
import com.epsi.covidmanager.model.beans.Token;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.webservice.APIService;
import com.epsi.covidmanager.model.webservice.RetrofitHttpUtilis;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(MockitoJUnitRunner.class)
public class DetailsSlotTest {

    @Mock
    DetailsSlot detailsSlot;

    private ArrayList<Vaccine> vaccines;

    private APIService apiService;

    @Before
    public void setUp() {
        apiService = RetrofitHttpUtilis.getRetrofitInstance().create(APIService.class);
        MockitoAnnotations.openMocks(this);

        doAnswer(invocation -> {
            apiService.getVaccines(Token.getToken()).enqueue(new Callback<List<Vaccine>>() {
                @Override
                public void onResponse(Call<List<Vaccine>> call, Response<List<Vaccine>> response) {
                    vaccines = (ArrayList<Vaccine>) response.body();
                    assertNotNull(vaccines);
                }

                @Override
                public void onFailure(Call<List<Vaccine>> call, Throwable t) {
                    assertFalse("Erreur : " + t.getMessage(), false);
                }
            });
            return null;
        }).when(detailsSlot).loadVaccines();
    }

    @Test
    public void loadVaccines() {
        detailsSlot.loadVaccines();
    }
}
