package com.epsi.covidmanager.entities;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Secretary extends RealmObject {
    @PrimaryKey
    private ObjectId _id = new ObjectId();

    @Required
    private String lastName;

    @Required
    private String login;

    @Required
    private String password;

    @Required
    private String firstName;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
