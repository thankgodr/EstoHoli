package com.richard.estoholi.models

data class HolidayResponse(var error : Boolean = false , var holidays :  Map<String, List<Holiday>>)


