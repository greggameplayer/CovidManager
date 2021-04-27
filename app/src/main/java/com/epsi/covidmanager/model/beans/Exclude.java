package com.epsi.covidmanager.model.beans;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;


public class Exclude implements ExclusionStrategy{
    public boolean shouldSkipClass(Class<?> clazz) {
        return true;
    }

    public boolean shouldSkipField(FieldAttributes field) {
        return true;
    }

}


