package com.epsi.covidmanager.model.beans;

import java.io.Serializable;

public class Slot implements Serializable {
    private static int NUMBER = 0;
    private String date;
    private int nb, id;

    public Slot(){
        date = "2020-12-03";
        nb = 120;
        id = NUMBER;
        NUMBER++;

    }

    public String getDate() {
        return date;
    }

    public int getNb() {
        return nb;
    }
    public int getId(){ return id; }
}
