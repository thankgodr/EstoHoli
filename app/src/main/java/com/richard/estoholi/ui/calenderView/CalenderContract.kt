package com.richard.estoholi.ui.calenderView

import android.content.Context
import com.richard.estoholi.ui.holidayList.adapter.SingleHolidayAdapter

interface CalenderContract {

    interface UI{
        fun returnedHoliday(adapter : SingleHolidayAdapter)
        fun getContext() : Context
        fun showPregress()
        fun hideProgress()


    }

    interface presenter{
       fun getHolidays(startDate :String)
    }
}