package com.richard.estoholi

import android.app.Application
import io.realm.Realm

class HolidayAplication : Application() {
    lateinit var realm : Realm
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        realm = Realm.getDefaultInstance()
    }
}