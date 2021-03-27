package com.epsi.covidmanager.model.beans;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Vial implements Serializable {
    private final String id;
    private int shotNumber;
    private final Vaccine vaccine;
    private Slot slot;

    public Vial(String _id, int shotNumber, Vaccine vaccine, Slot slot) {
        this.shotNumber = shotNumber;
        this.vaccine = vaccine;
        this.id = _id;
        this.slot = slot;
    }

    public Vial(String _id, int shotNumber, Vaccine vaccine) {
        this.shotNumber = shotNumber;
        this.vaccine = vaccine;
        this.id = _id;
        this.slot = null;
    }

    public Vial(int shotNumber, Vaccine vaccine) {
        this.shotNumber = shotNumber;
        this.vaccine = vaccine;
        this.id = null;
        this.slot = null;
    }

    public int getShotNumber() {
        return shotNumber;
    }

    public Vaccine getVaccine() {
        return vaccine;
    }

    public String getId() {
        return id;
    }

    public Slot getSlot() {
        return slot;
    }

    public static ArrayList<Vial> findAll(Context toastContext, ArrayList<Vaccine> vaccines, ArrayList<Slot> slots) {

        // Creates a new ParseQuery object to help us fetch MyCustomClass objects
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Vial");

        // Fetches data synchronously
        try {
            ArrayList<Vial> vials = new ArrayList<>();
            List<ParseObject> results = query.find();

            for(ParseObject result : results) {
                for(Vaccine vaccine : vaccines) {
                    if (vaccine.getId().equals(result.getString("vaccineId"))) {
                        for(Slot slot : slots) {
                            if (slot.getId().equals(result.getString("slotId"))) {
                                vials.add(new Vial(result.getObjectId(), result.getInt("shotNumber"), vaccine, slot));
                            }
                        }
                    }
                }
            }
            return vials;
        } catch (ParseException | IndexOutOfBoundsException e) {
            Toast.makeText(toastContext, "There are no vials", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public void updateShotNumber(int newValue) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Vial");
        try {
            this.shotNumber = newValue;
            query.get(this.id).put("shotNumber", this.shotNumber);
        } catch (ParseException e) {
            Log.d("Slot", "Update shotNumber problem");
        }
    }

    public void updateSlot(Slot newValue) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Vial");
        try {
            this.slot = newValue;
            query.get(this.id).put("slotId", this.getSlot().getId());
        } catch (ParseException e) {
            Log.d("Slot", "Update slot problem");
        }
    }

    public static boolean insert(Vial vial, Slot slot, Context toastContext) {
        ParseObject entity = new ParseObject("Vial");

        entity.put("shotNumber", vial.getShotNumber());
        entity.put("vaccineId", vial.getVaccine().getId());
        entity.put("slotId", vial.getSlot().getId());
        // Saves the new object.
        // Notice that the SaveCallback is totally optional!
        try {
            entity.save();
            return true;
        } catch (ParseException e) {
            Toast.makeText(toastContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
