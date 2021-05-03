package com.epsi.covidmanager;

import com.epsi.covidmanager.controller.formAddAndModifySlot;
import com.epsi.covidmanager.model.beans.Token;
import com.epsi.covidmanager.model.beans.User;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.beans.Vial;
import com.epsi.covidmanager.model.webservice.APIService;
import com.epsi.covidmanager.model.webservice.RetrofitHttpUtilis;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;


@RunWith(MockitoJUnitRunner.class)
public class FormTest {

    @Mock
    formAddAndModifySlot formAddAndModifySlot;

    private ArrayList<Vaccine> vaccines;

    private APIService apiService;

    private ArrayList<Vial> vialsByVaccine;

    @Before
    public void setUp() {
        apiService = RetrofitHttpUtilis.getRetrofitInstance().create(APIService.class);
        MockitoAnnotations.openMocks(this);

        doAnswer(invocation -> {
            apiService.getConnection(new User("visiteur@visiteur.fr", "visiteur")).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    apiService.getVaccines(Token.getToken()).enqueue(new Callback<List<Vaccine>>() {
                        @Override
                        public void onResponse(Call<List<Vaccine>> call, Response<List<Vaccine>> response) {
                            vaccines = (ArrayList<Vaccine>) response.body();
                            for (Vaccine vaccine : vaccines) {
                                if (vaccine.getName().equals("Moderna")) {

                                    apiService.getVialsByVaccineId(Token.getToken(), vaccine.getId()).enqueue(new Callback<List<Vial>>() {
                                        @Override
                                        public void onResponse(Call<List<Vial>> call, Response<List<Vial>> response) {
                                            vialsByVaccine = (ArrayList<Vial>) response.body();
                                            assertNotNull(vialsByVaccine);
                                        }

                                        @Override
                                        public void onFailure(Call<List<Vial>> call, Throwable t) {
                                            assertFalse("Erreur : " + t.getMessage(), false);
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Vaccine>> call, Throwable t) {
                            assertFalse("Erreur : " + t.getMessage(), false);
                        }
                    });
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    assertFalse("Erreur : " + t.getMessage(), false);
                }
            });
            return null;
        }).when(this.formAddAndModifySlot).getGoodVaccine();

        doAnswer(invocation -> {
            Date startTime = null;
            Date endTime = null;
            try {
                startTime = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(String.valueOf("03/05/2021 9:00"));
                endTime = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(String.valueOf("03/05/2021 11:00"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assertTrue(endTime.getTime() > startTime.getTime());
            return null;

        }).when(this.formAddAndModifySlot).checkDates();
    }


    @Test
    public void getVialsByVaccineId() {
        this.formAddAndModifySlot.getGoodVaccine();
    }

    @Test
    public void checkDates() {
        this.formAddAndModifySlot.checkDates();
    }
}
