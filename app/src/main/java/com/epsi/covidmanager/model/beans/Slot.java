package com.epsi.covidmanager.model.beans;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Slot implements Serializable {

    private static final Calendar CALENDAR = Calendar.getInstance();
    private static final DateFormat DATE_FORMAT =  new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    private final String _id;
    private Date startTime;
    private Date endTime;
    private boolean isActive;
    private int nbReservedPlaces;
    private int nbInitialPlaces;
    private Vial vial;

    public Slot(String _id, Date startTime, Date endTime, Boolean isActive, int nbReservedPlaces, int nbInitialPlaces, Vial vial){
        this.startTime = startTime;
        this.nbInitialPlaces = nbInitialPlaces;
        this.endTime = endTime;
        this.isActive = isActive;
        this.nbReservedPlaces = nbReservedPlaces;
        this.vial = vial;
        this._id = _id;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public Slot(Date startTime, Date endTime, Boolean isActive, int nbReservedPlaces,int nbInitialPlaces, Vial vial){
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = isActive;
        this.nbInitialPlaces = nbInitialPlaces;
        this.nbReservedPlaces = nbReservedPlaces;
        this.vial = vial;
        this._id = null;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }


    public void slotDuration(){

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
        Logger.w(elapsedDays + "days, " + elapsedHours +  "hours, " + elapsedMinutes + "d minutes, " + elapsedSeconds +"seconds");



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

    public Vial getVial() {
        return vial;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getDates(){
        SimpleDateFormat formater = new SimpleDateFormat("d/MM/yyyy");
        if (!formater.format(this.getStartTime()).equals(formater.format(this.getEndTime()))){
            return (formater.format(this.getStartTime()) +" - "+formater.format(this.getEndTime()));
        }
        return (formater.format(this.getStartTime()));
    };

    public static ArrayList<Slot> findAll(Context toastContext, ArrayList<Vial> vials) {

        // Creates a new ParseQuery object to help us fetch MyCustomClass objects
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Slot");

        // Fetches data synchronously
        try {
            ArrayList<Slot> slots = new ArrayList<>();
            List<ParseObject> results = query.find();

            for(ParseObject result : results) {
                for(Vial vial : vials) {
                    if (vial.getId().equals(result.getString("vialId"))) {
                        slots.add(new Slot(result.getObjectId(), result.getDate("startTime"), result.getDate("endTime"), result.getBoolean("isActive"), result.getInt("nbReservedPlaces"), result.getInt("nbInitialPlaces"), vial));
                    }
                }
            }
            return slots;
        } catch (ParseException | IndexOutOfBoundsException e) {
            Toast.makeText(toastContext, "There are no slots", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public void delete() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Slot");
        try {
            this.isActive = false;
            query.get(this._id).put("isActive", this.isActive);
        } catch (ParseException e) {
            Log.d("Slot", "Delete problem");
        }
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

    public void updateNbReservedPlaces(int newValue) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Slot");
        try {
            this.nbReservedPlaces = newValue;
            query.get(this._id).put("nbReservedPlaces", this.nbReservedPlaces);
        } catch (ParseException e) {
            Log.d("Slot", "Update nbReservedPlaces problem");
        }
    }

    public static boolean insert(Slot slot, Context toastContext) {
        ParseObject entity = new ParseObject("Slot");

        entity.put("startTime", slot.getStartTime());
        entity.put("endTime", slot.getEndTime());
        entity.put("nbReservedPlaces", slot.getNbReservedPlaces());
        entity.put("nbInitialPlaces", slot.getNbInitialPlaces());
        entity.put("vialId", slot.getVial().getId());
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