package com.bvaleo.taskpostapp.model;

import com.google.gson.annotations.Expose;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class Geo implements RealmModel {

    @Expose
    private String lat;
    @Expose
    private String lng;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

}