package com.richard.estoholi.ui.helpers

import android.util.Log.d
import java.text.SimpleDateFormat
import java.time.LocalDate
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
             while (today.get(Calendar.DAY_OF_WEEK) != weekOfDay+1) {
                 today.add(Calendar.DATE, 1);
                 d("okh", today.time.toString())
             }
             return format.format(today.time)
        }


        fun parseDate(dayString: String) :  Date{
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val c = Calendar.getInstance()
            c.time = format.parse(dayString + " 00:00:00")
             return c.time
        }

        fun getDayIt(key: String):String {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val date = format.parse(key)
            val dayFormater = SimpleDateFormat("EEEE")
            return dayFormater.format(date)
        }


    }
}