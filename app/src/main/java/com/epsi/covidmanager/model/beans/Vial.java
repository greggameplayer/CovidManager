package com.epsi.covidmanager.model.beans;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

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
                            } else {
                                vials.add(new Vial(result.getObjectId(), result.getInt("shotNumber"), vaccine));
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

    public void updateShotNumber(int newValue, SaveCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Vial");

        // Retrieve the object by id
        query.getInBackground(this.id, (object, e) -> {
            if (e == null) {
                //Object was successfully retrieved
                // Update the fields we want to
                this.shotNumber = newValue;
                object.put("shotNumber", this.getShotNumber());

                //All other fields will remain the same
                object.saveInBackground(callback);

            } else {
                // something went wrong
                Log.d("ERRORUPDATE", e.getMessage());
            }
        });
    }

    public void updateSlot(Slot newValue, SaveCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Vial");

        // Retrieve the object by id
        query.getInBackground(this.id, (object, e) -> {
            if (e == null) {
                //Object was successfully retrieved
                // Update the fields we want to
                this.slot = newValue;
                object.put("slotId", this.getSlot().getId());

                //All other fields will remain the same
                object.saveInBackground(callback);

            } else {
                // something went wrong
                Log.d("ERRORUPDATE", e.getMessage());
            }
        });
    }

    public static void insert(Vial vial, Slot slot, SaveCallback callback) {
        ParseObject entity = new ParseObject("Vial");

        entity.put("shotNumber", vial.getShotNumber());
        entity.put("vaccineId", vial.getVaccine().getId());
        entity.put("slotId", vial.getSlot().getId());
        entity.saveInBackground(callback);
    }

    public static void insert(Vial vial, SaveCallback callback) {
        ParseObject entity = new ParseObject("Vial");

        entity.put("shotNumber", vial.getShotNumber());
        entity.put("vaccineId", vial.getVaccine().getId());
        entity.saveInBackground(callback);
    }
}
