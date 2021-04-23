package com.epsi.covidmanager.model.webservice;

import com.epsi.covidmanager.model.beans.Slot;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ApiClass {

    public static final String GET_SLOT = "ajouter API";
    /*
    Add all requests
     */


    /*
    Add one function for each needs
     */

    public static ArrayList<Slot> getSlot() throws Exception {
        String result = OkHttpUtils.sendGetOkHttpRequest(GET_SLOT);
        ArrayList<Slot> slots = new ArrayList<>();
        Gson gson = new Gson();
        //Resultats resultats = gson.fromJson(result, Resultats.class);

        //if(resultats == null){
        //    throw new Exception("Aucun résultat");
        //}else if(resultats.getRecords() != null){
        //    for(Record record : resultats.getRecords()){
        //        if(record.getFields() != null){
        //            fields.add(record.getFields());
        //        }
        //    }
        //}

        return slots;
    }
}
