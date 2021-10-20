package com.richard.estoholi.models

import com.richard.estoholi.ui.helpers.Utils
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*


open class HolidayRealm(@PrimaryKey var day: String? = null, var holdays: RealmList<Holiday> = RealmList()) : RealmObject(){

    lateinit var dateTime :  Date
   fun setDate(){
       val format = SimpleDateFormat("yyyy-MM-dd")
       dateTime = format.parse(day)
   }
}
