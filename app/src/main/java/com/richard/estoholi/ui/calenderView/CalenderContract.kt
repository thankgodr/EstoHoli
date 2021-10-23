package com.richard.estoholi.ui.calenderView

import android.content.Context

interface CalenderContract {

    interface UI{
        fun returnedHoliday()
        fun getContext() : Context
        fun showPregress()

    }

    interface presenter{
       fun getHolidays(startDate :String)
    }
}