package com.epsi.covidmanager.model.beans;

import android.util.Log;

import com.epsi.covidmanager.model.mongo.Mongo;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import io.realm.RealmObject;
import io.realm.mongodb.App;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import io.realm.mongodb.mongo.result.InsertOneResult;
import io.realm.mongodb.mongo.result.UpdateResult;

public class Slot extends Document {

    ObjectId _id = new ObjectId();

    @BsonIgnore
    private static final Calendar CALENDAR = Calendar.getInstance();

    @BsonIgnore
    private static final DateFormat DATE_FORMAT =  new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    Date startTime;
    Date endTime;
    boolean isActive;
    int nbReservedPlaces;
    ObjectId vialId;

    public Slot(Date startTime, Date endTime, ObjectId vialId){
        this.vialId = vialId;
        this.isActive = true;
        this.nbReservedPlaces = 0;
        this.startTime = startTime;
        this.endTime = endTime;

    }

    @BsonIgnore
    public final void findAll(App.Callback<MongoCursor<Document>> callback) {
        Mongo mongo = Mongo.getInstance("covidmanager-wweml");
        List<Document> pipeline = Arrays.asList(new Document("$lookup", new Document("from", "vial").append("localField", "vialId").append("foreignField", "_id").append("as", "slot.vial")));
        MongoCollection<Document> collection = mongo.getDatabase().getCollection("slot");
        RealmResultTask<MongoCursor<Document>> findDocuments = collection.aggregate(pipeline).iterator();
        findDocuments.getAsync(callback);
    }

    @BsonIgnore
    public final void deleteById(ObjectId _id, App.Callback<UpdateResult> callback) {
        Mongo mongo = Mongo.getInstance("covidmanager-wweml");
        Document queryFilter = new Document("_partitionKey", "covidManager").append("_id", _id);
        Document updateDocument = new Document("isActive", false).append("_partitionKey", "covidManager");
        MongoCollection<Document> collection = mongo.getDatabase().getCollection("slot");
        collection.updateOne(queryFilter, updateDocument).getAsync(callback);
    }

    @BsonIgnore
    public final void updateNbReservedPlaces(ObjectId _id, int nbReservedPlaces, App.Callback<UpdateResult> callback) {
        Mongo mongo = Mongo.getInstance("covidmanager-wweml");
        Document queryFilter = new Document("_partitionKey", "covidManager").append("_id", _id);
        Document updateDocument = new Document("nbReservedPlaces", nbReservedPlaces).append("_partitionKey", "covidManager");
        MongoCollection<Document> collection = mongo.getDatabase().getCollection("slot");
        collection.updateOne(queryFilter, updateDocument).getAsync(callback);
    }

    @BsonIgnore
    public final void updateStartAndEndTime(ObjectId _id, Date startTime, Date endTime, App.Callback<UpdateResult> callback) {
        Mongo mongo = Mongo.getInstance("covidmanager-wweml");
        Document queryFilter = new Document("_partitionKey", "covidManager").append("_id", _id);
        Document updateDocument = new Document("startTime", startTime).append("endTime", endTime).append("_partitionKey", "covidManager");
        MongoCollection<Document> collection = mongo.getDatabase().getCollection("slot");
        collection.updateOne(queryFilter, updateDocument).getAsync(callback);
    }

    @BsonIgnore
    public final void insert(Slot slot, App.Callback<InsertOneResult> callback) {
        Mongo mongo = Mongo.getInstance("covidmanager-wweml");
        MongoCollection<Document> collection = mongo.getDatabase().getCollection("slot");
        collection.insertOne(slot).getAsync(callback);
    }


    public void slotDuration(){

        long test = endTime.getTime() - startTime.getTime();


        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = test / daysInMilli;
        test = test % daysInMilli;

        long elapsedHours = test / hoursInMilli;
        test = test % hoursInMilli;

        long elapsedMinutes = test / minutesInMilli;
        test = test % minutesInMilli;

        long elapsedSeconds = test / secondsInMilli;

        Log.w("TAG", DATE_FORMAT.format(startTime) +"");
        Log.w("TAG",  elapsedDays + "days, " + elapsedHours +  "hours, " + elapsedMinutes + "d minutes, " + elapsedSeconds +"seconds%n");
    }
    /*
    Getter and Setter
     */

}
