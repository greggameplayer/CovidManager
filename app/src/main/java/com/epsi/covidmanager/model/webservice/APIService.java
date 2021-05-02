package com.epsi.covidmanager.model.webservice;

import com.epsi.covidmanager.model.beans.Slot;
import com.epsi.covidmanager.model.beans.User;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.beans.Vial;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService{

    //METHOD ABOUT VACCINE
    @GET("vaccines")
    Call<List<Vaccine>> getVaccines(@Header("Authorization") String token);


    //METHOD ABOUT SLOT

    @GET("slots?filter[offset]=1")
    Call<List<Slot>> getSlots(@Header("Authorization") String token);

    @POST("slots")
    Call<Slot> createSlot(@Header("Authorization") String token, @Body Slot slot);

    @PATCH("slots/{idSlot}")
    Call<Slot> updateSlot(@Header("Authorization") String token, @Path("idSlot") int idSlot,@Body Slot slot);

    @DELETE("slots/{idSlot}")
    Call<Slot> deleteSlot(@Header("Authorization") String token, @Path("idSlot") int idSlot);




    //METHOD ABOUT VIAL

    @GET("vials?filter[include][0]=vaccine&filter[include][1]=slot")
    Call<List<Vial>> getVials(@Header("Authorization") String token);

    @GET("vials?filter[include][0]=vaccine&filter[include][1]=slot")
    Call<List<Vial>> getVialsByVaccineId(@Header("Authorization") String token, @Query("filter[where][idVaccine]") int idVaccine);

    @POST("vials")
    Call<Vial> createVial(@Header("Authorization") String token, @Body Vial vial);

    @PATCH("vials/{idVial}")
    Call<Vial> updateVial(@Header("Authorization") String token, @Path("idVial") int idVial, @Body Vial vial);



    //METHOD ABOUT USER

    @POST("users/login")
    Call<User> getConnection(@Body User user);

}
