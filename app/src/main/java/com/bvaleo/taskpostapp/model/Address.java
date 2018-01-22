package com.bvaleo.taskpostapp.model;

import com.google.gson.annotations.Expose;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class Address implements RealmModel {

    @Expose
    private String street;
    @Expose
    private String suite;
    @Expose
    private String city;
    @Expose
    private String zipcode;
    @Expose
    private Geo geo;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }
}
