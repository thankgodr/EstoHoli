package com.richard.estoholi.models

import io.realm.RealmList
import io.realm.RealmObject

data class HolidayResponse(var error : Boolean = false , var holidays :  Map<String, List<Holiday>?>? = null)


