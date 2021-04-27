package com.epsi.covidmanager.model.webservice;

import com.epsi.covidmanager.model.beans.Slot;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.beans.Vial;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService{

    //METHOD ABOUT VACCINE
    @GET("vaccines")
    Call<List<Vaccine>> getVaccines();


    //METHOD ABOUT SLOT

    @GET("slots?filter[offset]=1")
    Call<List<Slot>> getSlots();

    @POST("slots")
    Call<Slot> createSlot(@Field("startTime")Date startTime, @Field("endTime") Date endTime, @Field("nbInitialPlaces") int nbInitialPlaces, @Field("nbReservedPlaces") int nbReservedPlaces);

    @PATCH("slots/{idSlot}")
    Call<Slot> updateSlot(@Path("idSlot") int idSlot,@Field("startTime")Date startTime, @Field("endTime") Date endTime, @Field("nbInitialPlaces") int nbInitialPlaces, @Field("nbReservedPlaces") int nbReservedPlaces);

    @DELETE("slots/{idSlot}")
    Call<Slot> deleteSlot(@Path("idSlot") int idSlot);




    //METHOD ABOUT VIAL

    @GET("vials?filter[include][0]=vaccine&filter[include][1]=slot")
    Call<List<Vial>> getVials();

    @POST("vials")
    Call<Vial> createVial(@Body Vial vial);

    @PATCH("vials/{idVial}")
    Call<Vial> updateVial(@Path("idVial") int idVial, @Field("shotNumber")int shotNumber, @Field("idVaccine") int idVaccine, @Field("idSlot") int idSlot);

}
