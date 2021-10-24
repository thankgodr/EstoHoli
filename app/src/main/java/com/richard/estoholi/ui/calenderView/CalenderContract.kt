package com.richard.estoholi.ui.calenderView

import android.content.Context
import com.richard.estoholi.models.Holiday
import com.richard.estoholi.ui.holidayList.adapter.SingleHolidayAdapter

interface CalenderContract {

    interface UI{
        fun returnedHoliday(list : Map<String, List<Holiday>>)
        fun getContext() : Context
        fun showPregress()
        fun hideProgress()
        fun showError(err : String)


    }

    interface presenter{
       fun getHolidays(startDate :String)
    }
}