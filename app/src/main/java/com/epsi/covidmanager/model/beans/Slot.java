package com.epsi.covidmanager.model.beans;

import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import io.realm.RealmObject;

public class Slot extends RealmObject implements Serializable {

    private static final Calendar CALENDAR = Calendar.getInstance();
    private static final DateFormat DATE_FORMAT =  new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    private Date startTime, endTime;
    private boolean isActive;
    private int nbReservedPlaces;

    public Slot(){

        startTime = CALENDAR.getTime();
        CALENDAR.add(Calendar.HOUR, +3);
        endTime = CALENDAR.getTime();
        isActive = true;
        nbReservedPlaces = 0;
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

}