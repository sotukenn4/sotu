package com.example.myapplication

import android.app.Application
import android.widget.Spinner
import io.realm.Realm
import io.realm.RealmConfiguration

class MySchedulerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config= RealmConfiguration.Builder().allowWritesOnUiThread(true).build()
        Realm.setDefaultConfiguration(config)


    }
}