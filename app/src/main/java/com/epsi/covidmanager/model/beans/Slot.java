package com.epsi.covidmanager.model.beans;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Slot implements Serializable {

    private static final Calendar CALENDAR = Calendar.getInstance();
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    private final String _id;
    private Date startTime;
    private Date endTime;
    private boolean isActive;
    private int nbReservedPlaces;
    private int nbInitialPlaces;

    public Slot(String _id, Date startTime, Date endTime, Boolean isActive, int nbReservedPlaces, int nbInitialPlaces) {
        this.startTime = startTime;
        this.nbInitialPlaces = nbInitialPlaces;
        this.endTime = endTime;
        this.isActive = isActive;
        this.nbReservedPlaces = nbReservedPlaces;
        this._id = _id;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public Slot(Date startTime, Date endTime, Boolean isActive, int nbReservedPlaces, int nbInitialPlaces) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = isActive;
        this.nbInitialPlaces = nbInitialPlaces;
        this.nbReservedPlaces = nbReservedPlaces;
        this._id = null;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }


    public void slotDuration() {

        long test = endTime.getTime() - startTime.getTime();


        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = test / daysInMilli;
        test = test % daysInMilli;

        long elapsedHours = test / hoursInMilli;
        test = test % hoursInMilli;

        long elapsedMinutes = test / minutesInMilli;
        test = test % minutesInMilli;

        long elapsedSeconds = test / secondsInMilli;

        Logger.w(DATE_FORMAT.format(startTime));
        Logger.w(elapsedDays + "days, " + elapsedHours + "hours, " + elapsedMinutes + "d minutes, " + elapsedSeconds + "seconds");


    }
    /*
    Getter and Setter
     */

    public String getId() {
        return _id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getNbInitialPlaces(){
        return nbInitialPlaces;
    }

    public int getNbReservedPlaces() {
        return nbReservedPlaces;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getDates(){
        SimpleDateFormat formaterFull = new SimpleDateFormat("d/MM/yyyy hh:mm");
        SimpleDateFormat formaterDMY = new SimpleDateFormat("d/MM/yyyy");
        SimpleDateFormat formaterHM = new SimpleDateFormat("hh:mm");
        String date = formaterDMY.format(this.getStartTime())+ "   " + formaterHM.format(this.getStartTime()) + " - " + formaterHM.format(this.getEndTime());
        if (!formaterDMY.format(this.getStartTime()).equals(formaterDMY.format(this.getEndTime()))){
            date = formaterFull.format(this.getStartTime()) +" - "+formaterFull.format(this.getEndTime());
        }
        return (date);
    };

    public static ArrayList<Slot> findAll(Context toastContext, ArrayList<Vial> vials) {

        // Creates a new ParseQuery object to help us fetch MyCustomClass objects
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Slot");

        // Fetches data synchronously
        try {
            ArrayList<Slot> slots = new ArrayList<>();
            List<ParseObject> results = query.find();

            for (ParseObject result : results) {
                slots.add(new Slot(result.getObjectId(), result.getDate("startTime"), result.getDate("endTime"), result.getBoolean("isActive"), result.getInt("nbReservedPlaces"), result.getInt("nbInitialPlaces")));
            }
            return slots;
        } catch (ParseException | IndexOutOfBoundsException e) {
            Toast.makeText(toastContext, "There are no slots", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public void delete(SaveCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Slot");

        // Retrieve the object by id
        query.getInBackground(this._id, (object, e) -> {
            if (e == null) {
                //Object was successfully retrieved
                // Update the fields we want to
                this.isActive = false;
                object.put("isActive", this.isActive());

                //All other fields will remain the same
                object.saveInBackground(callback);

            } else {
                // something went wrong
                Log.d("ERRORUPDATE", e.getMessage());
            }
        });
    }

    public void updateStartTime(Date newValue) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Slot");
        try {
            this.startTime = newValue;
            query.get(this._id).put("startTime", this.startTime);
        } catch (ParseException e) {
            Log.d("Slot", "Update startTime problem");
        }
    }

    public void updateEndTime(Date newValue) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Slot");
        try {
            this.endTime = newValue;
            query.get(this._id).put("endTime", this.endTime);
        } catch (ParseException e) {
            Log.d("Slot", "Update endTime problem");
        }
    }

    public void updateNbReservedPlaces(int newValue, SaveCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Slot");

        // Retrieve the object by id
        query.getInBackground(this._id, (object, e) -> {
            if (e == null) {
                //Object was successfully retrieved
                // Update the fields we want to
                this.nbReservedPlaces = newValue;
                object.put("nbReservedPlaces", this.getNbReservedPlaces());

                //All other fields will remain the same
                object.saveInBackground(callback);

            } else {
                // something went wrong
                Log.d("ERRORUPDATE", e.getMessage());
            }
        });
    }

    public static boolean insert(Slot slot, Context toastContext) {
        ParseObject entity = new ParseObject("Slot");

        entity.put("startTime", slot.getStartTime());
        entity.put("endTime", slot.getEndTime());
        entity.put("nbReservedPlaces", slot.getNbReservedPlaces());
        entity.put("nbInitialPlaces", slot.getNbInitialPlaces());
        entity.put("isActive", slot.isActive());

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