package com.richard.estoholi.ui.calenderView

import android.view.View
import android.widget.TextView
import com.kizitonwose.calendarview.ui.ViewContainer
import com.richard.estoholi.R

class DayViewContainer(view: View) : ViewContainer(view) {
  val numHolidays = view.findViewById<TextView>(R.id.numHolidays)
}