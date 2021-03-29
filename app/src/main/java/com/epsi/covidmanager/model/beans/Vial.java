package com.epsi.covidmanager.model.beans;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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

    public static void findAll(ArrayList<Vaccine> vaccines, ArrayList<Slot> slots, AtomicReference<List<ParseObject>> newVials) {

        // Creates a new ParseQuery object to help us fetch MyCustomClass objects
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Vial");
        final FindCallback<ParseObject> lambda = (objects, e) -> {
            if (e == null) {
                newVials.set(objects);
            } else {
                Log.e("ERROR", e.getMessage());
            }
        };
        query.findInBackground(lambda);
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

    public void removeSlot(SaveCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Vial");

        query.getInBackground(this.id, (object, e) -> {
           if (e == null) {
               this.slot = null;
               object.remove("slotId");
               object.saveInBackground(callback);
           } else {
               Log.d("ERRORREMOVE", e.getMessage());
           }
        });
    }

    public void removeSlot(String vaccineId, SaveCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Vial");

        query.whereEqualTo("vaccineId", vaccineId);

        query.getInBackground(this.id, (object, e) -> {
            if (e == null) {
                this.slot = null;
                object.remove("slotId");
                object.saveInBackground(callback);
            } else {
                Log.d("ERRORREMOVE", e.getMessage());
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

    public static void insert(ArrayList<Vial> vials) {
        for (Vial vial : vials) {
            Vial.insert(vial, (el) -> {
            });
        }
    }

    public static Thread removeSlot(ArrayList<Vial> vials, String vaccineId, Object lockObject, AtomicInteger nb) {
        return new Thread(){
            @Override
            public void run() {
                synchronized (lockObject) {
                    for (Vial vial : vials) {
                        vial.removeSlot(vaccineId, e -> {
                            nb.getAndIncrement();
                        });
                    }
                    lockObject.notify();
                }
            }
        };
    }
}
