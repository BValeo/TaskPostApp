package com.bvaleo.taskpostapp.model;

import com.google.gson.annotations.Expose;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class Company implements RealmModel {

    @Expose
    private String name;
    @Expose
    private String catchPhrase;
    @Expose
    private String bs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatchPhrase() {
        return catchPhrase;
    }

    public void setCatchPhrase(String catchPhrase) {
        this.catchPhrase = catchPhrase;
    }

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }

}