package com.epsi.covidmanager;

import com.epsi.covidmanager.controller.SlotDashboard;
import com.epsi.covidmanager.model.beans.Slot;
import com.epsi.covidmanager.model.beans.Token;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.beans.Vial;
import com.epsi.covidmanager.model.webservice.APIService;
import com.epsi.covidmanager.model.webservice.RetrofitHttpUtilis;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class SlotDashboardTest {

    @Mock
    SlotDashboard slotDashboard;

    private ArrayList<Vial> vials;

    private ArrayList<Slot> slots;

    private ArrayList<Vaccine> vaccines;

    private APIService apiService;

    @Before
    public void setUp() {
        apiService = RetrofitHttpUtilis.getRetrofitInstance().create(APIService.class);
        MockitoAnnotations.openMocks(this);

        doAnswer(invocation -> {
            apiService.getVials(Token.getToken()).enqueue(new Callback<List<Vial>>() {
                @Override
                public void onResponse(Call<List<Vial>> call, Response<List<Vial>> response) {
                    vials = (ArrayList<Vial>) response.body();

                    assertNotNull(vials);

                    boolean mustBeAdded;

                    for(Vial vial : vials){
                        mustBeAdded = true;
                        if(vial.getSlot() != null){
                            for(Slot slot : slots)
                            {
                                if(slot.getId().equals(vial.getSlot().getId())){
                                    mustBeAdded = false;
                                    break;
                                }
                            }
                            if (mustBeAdded){
                                slots.add(vial.getSlot());
                            }
                        }
                    }
                    assertNotEquals(null, slots.size());
                }

                @Override
                public void onFailure(Call<List<Vial>> call, Throwable t) {
                    assertFalse("Erreur : " + t.getMessage(), false);
                }
            });
            return null;
        }).when(slotDashboard).loadVials();

        when(slotDashboard.quantityRemainToAllow(any())).thenCallRealMethod();
    }

    @Test
    public void loadVialsAndSlots() {
        slotDashboard.loadVials();
    }

    @Test
    public void quantityRemainToAllow() {
        int actual = slotDashboard.quantityRemainToAllow(vaccines.get(0));

        assertTrue(actual > 0);
    }
}
