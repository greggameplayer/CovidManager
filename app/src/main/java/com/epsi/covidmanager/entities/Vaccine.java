package com.epsi.covidmanager.entities;

import org.bson.Document;
import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Vaccine extends Document {
    ObjectId _id = new ObjectId();
    String _partitionKey;
    String name;

    public Vaccine(String _partitionKey, String name) {
        this._partitionKey = _partitionKey;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
