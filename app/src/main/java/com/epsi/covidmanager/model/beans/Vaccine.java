package com.epsi.covidmanager.model.beans;

import android.content.Context;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Vaccine implements Serializable {
    private final String id;
    private final String name;

    public Vaccine(String _id, String name) {
        this.name = name;
        this.id = _id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public static ArrayList<Vaccine> findAll(Context toastContext) {

        // Creates a new ParseQuery object to help us fetch MyCustomClass objects
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Vaccine");

        // Fetches data synchronously
        try {
            ArrayList<Vaccine> vaccines = new ArrayList<>();
            List<ParseObject> results = query.find();

            for(ParseObject result : results) {
                vaccines.add(new Vaccine(result.getObjectId(), result.getString("name")));
            }
            return vaccines;
        } catch (ParseException | IndexOutOfBoundsException e) {
            Toast.makeText(toastContext, "There are no vaccine", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
