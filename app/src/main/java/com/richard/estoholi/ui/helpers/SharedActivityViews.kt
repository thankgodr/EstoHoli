package com.richard.estoholi.ui.helpers

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.beardedhen.androidbootstrap.BootstrapDropDown
import com.beardedhen.androidbootstrap.BootstrapText
import com.richard.estoholi.R
import com.richard.estoholi.ui.calenderView.CalenderActivity
import com.richard.estoholi.ui.holidayList.HolidaytList
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import java.time.DayOfWeek

open class SharedActivityViews{
    val isCal : Boolean
    constructor(isCalenda: Boolean){
        isCal = isCalenda
    }

     fun setUpBottomBar(startDate : String, context : Activity, bottom_navigation_bar : BottomNavigationBar,changeDay: ChangeDay){
            bottom_navigation_bar
                .addItem( BottomNavigationItem(R.drawable.ic_calender_white, "Calenda"))
                .addItem( BottomNavigationItem(R.drawable.ic_listview_white, "List view"))
                .addItem(BottomNavigationItem(R.drawable.ic_settings_white, "Settings"))
                .setFirstSelectedPosition(if(isCal) 0 else 1)
                .initialise();


        bottom_navigation_bar.setTabSelectedListener(object :
            BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {
                if(isCal){
                    if(position == 1){
                        val act = Intent(context, HolidaytList::class.java)
                        act.putExtra("startDate", startDate)
                        context.startActivity(act)
                        context.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right)

                    }
                }else{
                    if(position == 0){
                        val act = Intent(context, CalenderActivity::class.java)
                        act.putExtra("startDate", startDate)
                        context.startActivity(act)
                        context. overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave)

                    }
                }
                if(position == 2){
                    startSettings(context, bottom_navigation_bar, changeDay)
                }
            }

            override fun onTabUnselected(position: Int) {
                Log.d("okh", "Unlicked position $position")
            }

            override fun onTabReselected(position: Int) {
                Log.d("okh", "Reselected position $position")
            }

        })
    }



    fun startSettings(activity: Activity, bottom_navigation_bar: BottomNavigationBar, changeDay: ChangeDay){

        activity.run{
            alert{
                 var selected: String = "Sunday"
                val view = layoutInflater.inflate(R.layout.settings_ui,null)
               val firstDaySelect = view.findViewById<BootstrapDropDown>(R.id.firstDaySelect)
                firstDaySelect.bootstrapText = BootstrapText.Builder(activity).addText(selected).build()
                firstDaySelect.setTextColor(activity.resources.getColor(R.color.black))
                firstDaySelect.setOnDropDownItemClickListener { parent, v, id -> run{
                     selected = activity.resources.getStringArray(R.array.bootstrap_dropdown_example_data)[id]
                    firstDaySelect.bootstrapText = BootstrapText.Builder(activity).addText(selected).build()
                    firstDaySelect.setTextColor(activity.resources.getColor(R.color.black))

                } }

                title = "Select First Day"
                customView = view
                isCancelable = false
                positiveButton(buttonText = "Save", onClicked = {
                    changeDay.updateNewFirstDay(getDayOfWeek(selected, activity))
                    if(isCal){
                        bottom_navigation_bar.selectTab(0)
                    }else{
                        bottom_navigation_bar.selectTab(1)
                    }
                    it.dismiss()
                })

                negativeButton(buttonText = "Cancel", onClicked = {
                    if(isCal){
                        bottom_navigation_bar.selectTab(0)
                    }else{
                        bottom_navigation_bar.selectTab(1)
                    }
                    it.dismiss()
                })


            }.show()
        }
    }

    private fun getDayOfWeek(day: String, activity: Activity): DayOfWeek {
        var arr = activity.resources.getStringArray(R.array.bootstrap_dropdown_example_data)
         when (day) {
            arr[0] -> return DayOfWeek.MONDAY
            arr[1] -> return DayOfWeek.TUESDAY
            arr[2] -> return DayOfWeek.WEDNESDAY
            arr[3] -> return DayOfWeek.THURSDAY
            arr[4] -> return DayOfWeek.FRIDAY
            arr[5] -> return DayOfWeek.SATURDAY
            arr[6] -> return DayOfWeek.SUNDAY
        }
        return DayOfWeek.SUNDAY
    }
}