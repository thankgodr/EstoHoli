package com.richard.estoholi.ui.calenderView

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import com.richard.estoholi.R
import com.richard.estoholi.ui.holidayList.HoldayContract
import com.richard.estoholi.ui.holidayList.adapter.HoldayListAdapter
import com.richard.estoholi.ui.holidayList.adapter.SingleHolidayAdapter
import kotlinx.android.synthetic.main.calenda_view.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*


class CalenderActivity : AppCompatActivity() , CalenderContract.UI{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calenda_view)

        maincalenda.dayBinder = object : DayBinder<DayViewContainer>{
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.numHolidays.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    container.numHolidays.setTextColor(Color.WHITE)
                } else {
                    container.numHolidays.setTextColor(Color.GRAY)
                }
            }

            override fun create(view: View): DayViewContainer {
                return DayViewContainer(view)
            }
        }

        maincalenda.monthScrollListener = { month ->
            val title = "${DateTimeFormatter.ofPattern("MMMM").format(month.yearMonth)} ${month.yearMonth.year}"
            exFiveMonthYearText.text = title
        }

        maincalenda.monthHeaderBinder =  object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                // Setup each header day text if we have not done that already.
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = month.yearMonth
                    container.legendLayout.children.map { it as TextView }.forEachIndexed { index, tv ->
                        tv.text = daysOfWeekFromLocale()[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                            .toUpperCase(Locale.ENGLISH)
                        tv.setTextColor(resources.getColor(R.color.text_grey))
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                    }
                    month.yearMonth
                }
            }
        }

        exFiveNextMonthImage.setOnClickListener {
            maincalenda.findFirstVisibleMonth()?.let {
                maincalenda.smoothScrollToMonth(it.yearMonth.next)
            }
        }

        exFivePreviousMonthImage.setOnClickListener {
            maincalenda.findFirstVisibleMonth()?.let {
                maincalenda.smoothScrollToMonth(it.yearMonth.previous)
            }
        }

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        maincalenda.setup(firstMonth, lastMonth, firstDayOfWeek)
        maincalenda.scrollToMonth(currentMonth)
    }

    fun daysOfWeekFromLocale(): Array<DayOfWeek> {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        var daysOfWeek = DayOfWeek.values()
        // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
        // Only necessary if firstDayOfWeek != DayOfWeek.MONDAY which has ordinal 0.
        if (firstDayOfWeek != DayOfWeek.MONDAY) {
            val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
            val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
            daysOfWeek = rhs + lhs
        }
        return daysOfWeek
    }

    override fun returnedHoliday(adapter: SingleHolidayAdapter) {

    }

    override fun getContext(): Context {
        return this
    }

    override fun showPregress() {
        TODO("Not yet implemented")
    }

    override fun hideProgress() {
        TODO("Not yet implemented")
    }


}