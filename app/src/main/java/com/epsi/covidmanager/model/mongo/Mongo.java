package com.epsi.covidmanager.model.mongo;

import android.util.Log;

import com.epsi.covidmanager.entities.Vaccine;

import org.bson.Document;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import io.realm.mongodb.sync.SyncConfiguration;

public final class Mongo implements Serializable {
    private static Mongo instance;
    private final App app;
    private final AtomicReference<User> user;
    private SyncConfiguration config;
    private MongoClient client;
    private MongoDatabase database;

    private Mongo(String appId) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        app = new App(new AppConfiguration.Builder(appId)
                .build());
        user = new AtomicReference<>();
    }

    public static Mongo getInstance(String appId) {
        if (instance == null) {
            instance = new Mongo(appId);
        }
        return instance;
    }

    public void login (String email, String password, App.Callback<User> callback) {
        Credentials emailPasswordCredentials = Credentials.emailPassword(email, password);
        app.loginAsync(emailPasswordCredentials, callback);
    }

    /* public void writeAstraZenecaLocally() {
        Realm.getInstanceAsync(config, new Realm.Callback() {
            @Override
            public void onSuccess(Realm realm) {
                Log.v("EXAMPLE", "Successfully opened a realm.");
                // Read all tasks in the realm. No special syntax required for synced realms.
                RealmResults<Vaccine> vaccines = realm.where(Vaccine.class).findAll();
                Log.d("MONGOTEST", vaccines.toString());
                // Write to the realm. No special syntax required for synced realms.
                realm.executeTransaction(r -> {
                    Vaccine vaccine = new Vaccine();
                    vaccine.setName("AstraZeneca");
                    r.insert(vaccine);
                });
                // Don't forget to close your realm!
                realm.close();
            }
        });
    } */

    public void findAll(String collection) {
        //Document queryFilter = new Document("_partitionKey", "CovidManager").append("name", "value");
        Document queryFilter = new Document("_partitionKey", "CovidManager");
        MongoCollection<Document> collection1 = database.getCollection(collection);
        RealmResultTask<MongoCursor<Document>> findTask = collection1.find(queryFilter).iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                Log.v("EXAMPLE", "successfully found all vaccines :");
                while (results.hasNext()) {
                    Log.v("EXAMPLE", results.next().toString());
                }
            } else {
                Log.e("EXAMPLE", "failed to find documents with: ", task.getError());
            }
        });
    }

    public void updateOneVaccine(String collection, String vaccineName, Vaccine newVaccine) {
        Document queryFilter = new Document("name", vaccineName).append("_partitionKey", "CovidManager");
        Document updateDocument = new Document("name", newVaccine.getName()).append("_partitionKey", "CovidManager");
        MongoCollection<Document> mongoCollection = database.getCollection(collection);
        mongoCollection.updateOne(queryFilter, updateDocument).getAsync(task -> {
            if (task.isSuccess()) {
                long count = task.get().getModifiedCount();
                if (count == 1) {
                    Log.v("EXAMPLE", "successfully updated a document.");
                } else {
                    Log.v("EXAMPLE", "did not update a document.");
                }
            } else {
                Log.e("EXAMPLE", "failed to update document with: ", task.getError());
            }
        });
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void setClient(MongoClient client) {
        this.client = client;
    }

    public void setDatabase(MongoDatabase database) {
        this.database = database;
    }

    public MongoClient getClient() {
        return client;
    }

    public AtomicReference<User> getUser() {
        return user;
    }

    public App getApp() {
        return app;
    }
}
