package com.richard.estoholi

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class HolidayAplication : Application() {
    lateinit var realm : Realm
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config =  RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .build()
        //val realmb = Realm.getInstance(config)
        realm = Realm.getInstance(config)
    }
}