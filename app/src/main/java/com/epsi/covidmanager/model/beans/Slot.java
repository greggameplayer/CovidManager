package com.epsi.covidmanager.model.beans;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Slot implements Serializable {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");


    @SerializedName("idSlot")
    @Expose
    private Integer idSlot;

    @SerializedName("startTime")
    @Expose
    private Date startTime;

    @SerializedName("endTime")
    @Expose
    private Date endTime;

    @SerializedName("nbReservedPlaces")
    @Expose
    private Integer nbReservedPlaces;

    @SerializedName("nbInitialPlaces")
    @Expose
    private Integer nbInitialPlaces;

    public Slot(Integer _id, Date startTime, Date endTime, int nbReservedPlaces, int nbInitialPlaces) {
        this.startTime = startTime;
        this.nbInitialPlaces = nbInitialPlaces;
        this.endTime = endTime;
        this.nbReservedPlaces = nbReservedPlaces;
        this.idSlot = _id;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public Slot(Date startTime, Date endTime, int nbReservedPlaces, int nbInitialPlaces) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.nbInitialPlaces = nbInitialPlaces;
        this.nbReservedPlaces = nbReservedPlaces;
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
    Getter
     */

    public Integer getId() {
        return idSlot;
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

    public void setNbReservedPlaces(Integer nbReservedPlaces) {
        this.nbReservedPlaces = nbReservedPlaces;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setNbInitialPlaces(Integer nbInitialPlaces) {
        this.nbInitialPlaces = nbInitialPlaces;
    }

    public String getDates(){
        SimpleDateFormat formaterFull = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        SimpleDateFormat formaterDMY = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formaterHM = new SimpleDateFormat("hh:mm");
        String date = formaterDMY.format(this.getStartTime())+ "   " + formaterHM.format(this.getStartTime()) + " - " + formaterHM.format(this.getEndTime());
        if (!formaterDMY.format(this.getStartTime()).equals(formaterDMY.format(this.getEndTime()))){
            date = formaterFull.format(this.getStartTime()) +" - "+formaterFull.format(this.getEndTime());
        }
        return (date);
    }
}