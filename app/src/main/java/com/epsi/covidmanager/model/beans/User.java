package com.epsi.covidmanager.model.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("token")
    @Expose(serialize = false)
    private String token;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getToken() {
        return token;
    }
}
