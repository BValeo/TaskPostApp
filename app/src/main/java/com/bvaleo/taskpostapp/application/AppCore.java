package com.bvaleo.taskpostapp.application;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Valery on 22.01.2018.
 */

public class AppCore extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("user.realm").build();
        Realm.setDefaultConfiguration(config);
    }
}
