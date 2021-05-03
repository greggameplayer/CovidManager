package com.epsi.covidmanager.model.beans;

import android.content.Context;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.util.Objects;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Secretary implements Serializable {
    String objectId;
    String login;
    String password;
    String lastname;
    String firstname;

    public Secretary(String objectId, String login, String password, String lastname, String firstname) {
        this.objectId = objectId;
        this.login = login;
        this.password = password;
        this.lastname = lastname;
        this.firstname = firstname;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public static Secretary login(String login, String password, Context toastContext) {

        // Creates a new ParseQuery object to help us fetch MyCustomClass objects
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Secretary");

        // Fetches data synchronously
        try {
            ParseObject result = query.whereEqualTo("login", login).setLimit(1).find().get(0);
            if(BCrypt.verifyer(BCrypt.Version.VERSION_2A).verify(password.toCharArray(), Objects.requireNonNull(result.getString("password"))).verified) {
                return new Secretary(result.getObjectId(), result.getString("login"), result.getString("password"), result.getString("firstname"), result.getString("lastname"));
            } else {
                Toast.makeText(toastContext, "Wrong password", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException | IndexOutOfBoundsException e) {
            Toast.makeText(toastContext, "This user doesn't exist", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
