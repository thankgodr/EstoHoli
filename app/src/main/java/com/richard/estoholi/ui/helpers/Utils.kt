package com.richard.estoholi.ui.helpers

import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object{
        fun getNextDate(startDate: String): String {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val c = Calendar.getInstance()
            c.time = format.parse(startDate)
            c.add(Calendar.DATE, 30)
            val endDate =format.format(c.time)
            return endDate
        }

        fun getNextDate(startDate: String, days: Int): String {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val c = Calendar.getInstance()
            c.time = format.parse(startDate)
            c.add(Calendar.DATE, days)
            val endDate =format.format(c.time)
            return endDate
        }

        fun getPreviousDate(startDate: String): String {
            return getNextDate(startDate, -1)
        }

        fun getTomorowDates(startDate: String): String {
            return getNextDate(startDate, 1)
        }

        fun getTheNext30Days(startDate: String): String {
            return getNextDate(startDate, 30)
        }

        fun getPrevious30Days(startDate: String): String {
            return getNextDate(startDate, -30)
        }

        fun getTodaysDate(): String {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val date = Date()
           return  format.format(date)
        }


         fun getNextweekOfDay(weekOfDay: Int, startDate: String): String {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val today = Calendar.getInstance()
            today.time = format.parse(startDate)
            val dayOfWeek = today[Calendar.DAY_OF_WEEK]
            var daysUntilNextWeekOfDay = weekOfDay - dayOfWeek
            if (daysUntilNextWeekOfDay == 0) daysUntilNextWeekOfDay = 7
            val nextWeekOfDay = today.clone() as Calendar
            nextWeekOfDay.add(Calendar.DAY_OF_WEEK, daysUntilNextWeekOfDay)
            return format.format(nextWeekOfDay.time)
        }


        fun parseDate(dayString: String) :  Date{
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val c = Calendar.getInstance()
            c.time = format.parse(dayString + " 00:00:00")
             return c.time
        }



    }
}