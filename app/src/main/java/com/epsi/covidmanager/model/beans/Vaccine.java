package com.epsi.covidmanager.model.beans;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;


public class Vaccine implements Serializable {
    @SerializedName("idVaccine")
    @Expose
    private Integer idVaccine;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getId() {
        return idVaccine;
    }

    public void setId(Integer idVaccine) {
        this.idVaccine = idVaccine;
    }

    public String getName() {
        return name;
    }
}
