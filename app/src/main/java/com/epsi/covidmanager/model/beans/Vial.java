package com.epsi.covidmanager.model.beans;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class Vial implements Serializable {
    @SerializedName("idVial")
    @Expose(serialize = false)
    private int idVial;

    @SerializedName("shotNumber")
    @Expose
    private int shotNumber;

    @SerializedName("vaccine")
    @Expose(serialize = false)
    private Vaccine vaccine;

    @SerializedName("slot")
    @Expose(serialize = false)
    private Slot slot;

    @SerializedName("idSlot")
    @Expose
    private int idSlot;

    @SerializedName("idVaccine")
    @Expose
    private int idVaccine;



    public Vial(int _id, int shotNumber, Vaccine vaccine, Slot slot) {
        this.shotNumber = shotNumber;
        this.vaccine = vaccine;
        this.idVial = _id;
        this.slot = slot;
        this.idVaccine = vaccine.getId();
        this.idSlot = slot.getId();
    }

    public Vial(int _id, int shotNumber, Vaccine vaccine) {
        this.shotNumber = shotNumber;
        this.vaccine = vaccine;
        this.idVial = _id;
        this.idVaccine = vaccine.getId();
    }

    public Vial(int shotNumber, Vaccine vaccine) {
        this.shotNumber = shotNumber;
        this.vaccine = vaccine;
        this.idVaccine = vaccine.getId();
    }

    public int getShotNumber() {
        return shotNumber;
    }

    public Vaccine getVaccine() {
        return vaccine;
    }

    public int getId() {
        return idVial;
    }

    public Slot getSlot() {
        return slot;
    }




}
