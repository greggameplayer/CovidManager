package com.epsi.covidmanager.model.beans;

import com.epsi.covidmanager.model.mongo.Mongo;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

import io.realm.mongodb.App;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class Vaccine extends Document {
    ObjectId _id = new ObjectId();
    String _partitionKey;
    String name;

    public Vaccine(String _partitionKey, String name) {
        this._partitionKey = _partitionKey;
        this.name = name;
    }

    @BsonIgnore
    public final void findAll(App.Callback<MongoCursor<Document>> callback) {
        Mongo mongo = Mongo.getInstance("covidmanager-wweml");
        Document queryFilter = new Document("_partitionKey", "covidManager");
        MongoCollection<Document> collection = mongo.getDatabase().getCollection("vaccine");
        RealmResultTask<MongoCursor<Document>> findDocuments = collection.find(queryFilter).iterator();
        findDocuments.getAsync(callback);
    }

    @BsonIgnore
    public final void findByName(String name, App.Callback<MongoCursor<Document>> callback) {
        Mongo mongo = Mongo.getInstance("covidmanager-wweml");
        Document queryFilter = new Document("_partitionKey", "covidManager").append("name", name);
        MongoCollection<Document> collection = mongo.getDatabase().getCollection("vaccine");
        RealmResultTask<MongoCursor<Document>> findDocuments = collection.find(queryFilter).iterator();
        findDocuments.getAsync(callback);
    }
}