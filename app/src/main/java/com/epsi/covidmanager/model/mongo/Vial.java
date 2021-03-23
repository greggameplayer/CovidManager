package com.epsi.covidmanager.model.mongo;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;

import io.realm.mongodb.App;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import io.realm.mongodb.mongo.result.UpdateResult;

public class Vial extends Document {
    ObjectId _id = new ObjectId();
    String _partitionKey;
    int shotNumber;
    ObjectId vaccineId;

    public Vial(String _partitionKey, int shotNumber, ObjectId vaccineId) {
        this._partitionKey = _partitionKey;
        this.shotNumber = shotNumber;
        this.vaccineId = vaccineId;
    }

    @BsonIgnore
    public static void findAll(App.Callback<MongoCursor<Document>> callback) {
        Mongo mongo = Mongo.getInstance("covidmanager-wweml");
        List<Document> pipeline = Arrays.asList(new Document("$lookup", new Document("from", "vaccine").append("localField", "vaccineId").append("foreignField", "_id").append("as", "vial.vaccine")));
        MongoCollection<Document> collection = mongo.getDatabase().getCollection("vial");
        RealmResultTask<MongoCursor<Document>> findDocuments = collection.aggregate(pipeline).iterator();
        findDocuments.getAsync(callback);
    }

    @BsonIgnore
    public static void findById(ObjectId id, App.Callback<MongoCursor<Document>> callback) {
        Mongo mongo = Mongo.getInstance("covidmanager-wweml");
        List<Document> pipeline = Arrays.asList(new Document("$match", new Document("_id", id)),new Document("$lookup", new Document("from", "vaccine").append("localField", "vaccineId").append("foreignField", "_id").append("as", "vial.vaccine")));
        MongoCollection<Document> collection = mongo.getDatabase().getCollection("vial");
        RealmResultTask<MongoCursor<Document>> findDocuments = collection.aggregate(pipeline).iterator();
        findDocuments.getAsync(callback);
    }

    @BsonIgnore
    public static void updateShotNumber(ObjectId _id, int shotNumber, App.Callback<UpdateResult> callback) {
        Mongo mongo = Mongo.getInstance("covidmanager-wweml");
        Document queryFilter = new Document("_id", _id).append("_partitionKey", "covidManager");
        Document updateDocument = new Document("shotNumber", shotNumber).append("_partitionKey", "covidManager");
        MongoCollection<Document> collection = mongo.getDatabase().getCollection("vial");
        collection.updateOne(queryFilter, updateDocument).getAsync(callback);
    }
}
