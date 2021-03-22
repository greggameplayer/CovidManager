package com.epsi.covidmanager;

import android.util.Log;

import com.epsi.covidmanager.entities.Vaccine;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.RealmResults;
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

public class Mongo {
    private final App app;
    private final AtomicReference<User> user;
    private SyncConfiguration config;
    private MongoClient client;
    private MongoDatabase database;

    public Mongo(String appId, String database) {
        app = new App(new AppConfiguration.Builder(appId)
                .build());
        Credentials anonymousCredentials = Credentials.anonymous();
        user = new AtomicReference<>();
        app.loginAsync(anonymousCredentials, it -> {
            if (it.isSuccess()) {
                Log.v("AUTH", "Successfully authenticated anonymously.");
                user.set(app.currentUser());
                client = user.get().getMongoClient("mongodb-atlas");
                this.database = client.getDatabase(database);

                config = new SyncConfiguration.Builder(user.get(), "CovidManager")
                        .allowQueriesOnUiThread(true)
                        .allowWritesOnUiThread(true)
                        .build();
            } else {
                Log.e("AUTH", it.getError().toString());
            }
        });
    }

    public AtomicReference<User> getUser() {
        return user;
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
}
