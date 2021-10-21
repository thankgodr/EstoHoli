package com.richard.estoholi.models

import io.realm.RealmObject

open class Holiday(var name : String? = null, var type : String? = null) : RealmObject()
