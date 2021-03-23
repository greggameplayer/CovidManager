package com.epsi.covidmanager.model.beans;

import android.util.Log;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
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

        Log.w("TAG", DATE_FORMAT.format(startTime) +"");
        Log.w("TAG",  elapsedDays + "days, " + elapsedHours +  "hours, " + elapsedMinutes + "d minutes, " + elapsedSeconds +"seconds%n");



    }
    /*
    Getter and Setter
     */

}
