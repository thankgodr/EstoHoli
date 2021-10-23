package com.richard.estoholi.ui.calenderView

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.kizitonwose.calendarview.ui.ViewContainer
import com.richard.estoholi.R

class DayViewContainer(view: View) : ViewContainer(view) {
  val numHolidays = view.findViewById<TextView>(R.id.numHolidays)
  val container = view.findViewById<ConstraintLayout>(R.id.exFiveDayLayout)
  val holidayTop = view.findViewById<View>(R.id.holidaTop)
  val holidayBottom = view.findViewById<View>(R.id.holidayButtom)
}