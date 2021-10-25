package com.richard.estoholi.ui.calenderView

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.util.TypedValue
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import com.richard.estoholi.R
import com.richard.estoholi.models.Holiday
import com.richard.estoholi.ui.calenderView.adapters.CalenderHolidayAdapter
import com.richard.estoholi.ui.helpers.ChangeDay
import com.richard.estoholi.ui.helpers.CollapserAnim
import com.richard.estoholi.ui.helpers.SharedActivityViews
import com.richard.estoholi.ui.helpers.Utils
import com.richard.estoholi.ui.holidayList.HoldayContract
import com.richard.estoholi.ui.holidayList.HolidaytList
import kotlinx.android.synthetic.main.calenda_view.*
import kotlinx.android.synthetic.main.calendar_day.*
import org.jetbrains.anko.alert
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*


class CalenderActivity : AppCompatActivity(), CalenderContract.UI{

    lateinit var presenter : CalenderPresenter
    val sharedActivityViews = SharedActivityViews(true)
    lateinit var progresBar: ProgressDialog
    var oldCalendayView : DayViewContainer? = null

    private var selectedDate: LocalDate? = null

    var startDateGen = "";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calenda_view)

        supportActionBar!!.hide()
        attachPresenter()
        UiSetup()

        sharedActivityViews.setUpBottomBar(startDateGen, this, bottom_navigation_bar, object :
            ChangeDay {
            override fun updateNewFirstDay(week: DayOfWeek) {
                renderCalendar(week)
            }

        })
    }

    private fun UiSetup(){
        progresBar = ProgressDialog(this)
        maincalenda.dayBinder = object : DayBinder<DayViewContainer>{
            override fun bind(container: DayViewContainer, day: CalendarDay) {

                var dateString = Utils.localDateTOString(day.date)

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
            startDateGen = "${month.yearMonth.year}-${month.month}-01"
            presenter.getHolidays(startDateGen)
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

        renderCalendar(DayOfWeek.SUNDAY)

    }

    private fun renderCalendar(dayofWeek: DayOfWeek){

        maincalenda.monthHeaderBinder =  object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                // Setup each header day text if we have not done that already.
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = month.yearMonth
                    container.legendLayout.children.map { it as TextView }.forEachIndexed { index, tv ->
                        tv.text = daysOfWeekFromLocale(dayofWeek)[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                            .toUpperCase(Locale.ENGLISH)
                        tv.setTextColor(resources.getColor(R.color.text_grey))
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                    }
                    month.yearMonth
                }
            }
        }



        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(1000)
        val lastMonth = currentMonth.plusMonths(1000)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        maincalenda.setup(firstMonth, lastMonth, dayofWeek)
        maincalenda.scrollToMonth(currentMonth)
    }








    private fun attachPresenter() {
        presenter = CalenderPresenter(this)
    }

    fun daysOfWeekFromLocale(dayofWeek : DayOfWeek = DayOfWeek.SUNDAY): Array<DayOfWeek> {
        val firstDayOfWeek = WeekFields.of(dayofWeek, 7).firstDayOfWeek
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

    override fun returnedHoliday(map: Map<String, List<Holiday>>) {
        updateDateBinderWithHolidays(map)
    }

    override fun getContext(): Context {
        return this
    }

    override fun showPregress() {
        if(progresBar == null){
            progresBar = ProgressDialog(this)
        }
        progresBar.setTitle("Loading...")
        progresBar.show()
    }

    override fun hideProgress() {
        progresBar.hide()
    }

    override fun showError(err: String) {
        alert{
            isCancelable = false
            title = this.ctx.getString(R.string.errorTitle)
            message = err
            positiveButton(this.ctx.getString(R.string.closeString), {
                it.cancel()
            })
        }.show()
    }


    private fun updateDateBinderWithHolidays(data : Map<String, List<Holiday>>){
        maincalenda.dayBinder = object : DayBinder<DayViewContainer>{
            override fun bind(container: DayViewContainer, day: CalendarDay) {



                var dateString = Utils.localDateTOString(day.date)

                if(!data.get(dateString).isNullOrEmpty()){
                    if(data.get(dateString)!!.size == 1){
                        container.holidayTop.visibility = View.VISIBLE
                    }
                    else if(data.get(dateString)!!.size > 1){
                        container.holidayBottom.visibility = View.VISIBLE
                        container.holidayTop.visibility = View.VISIBLE
                    }

                }

                container.container.setOnClickListener({
                        removeBackGroun(oldCalendayView,container )
                    if(!data.get(dateString).isNullOrEmpty()){
                        CollapserAnim.expand(recyclerView)
                        val adapter = CalenderHolidayAdapter(Utils.parseDate(dateString), data.get(dateString)!!, this@CalenderActivity)
                        recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                        recyclerView.layoutManager = LinearLayoutManager(this@CalenderActivity,LinearLayoutManager.VERTICAL,false)
                    }else{
                        CollapserAnim.collapse(recyclerView)
                    }

                })


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

    }

    private fun removeBackGroun(oldview : DayViewContainer?, newView : DayViewContainer ){
       if(oldview != null){
           oldview.container.setBackgroundColor(this.resources.getColor(R.color.item_view_bg_color))
           newView.container.setBackgroundColor(this.resources.getColor(R.color.bootstrap_brand_danger))
           oldCalendayView = newView
       }else{
           newView.container.setBackgroundColor(this.resources.getColor(R.color.bootstrap_brand_danger))
           oldCalendayView = newView
       }
    }

}