package com.epsi.covidmanager.model.beans;

public class Slot {
    private String date;
    private int nb;

    public Slot(){
        date = "2020-12-03";
        nb = 120;
    }

    public String getDate() {
        return date;
    }

    public int getNb() {
        return nb;
    }
}
