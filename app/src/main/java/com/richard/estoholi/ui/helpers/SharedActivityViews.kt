package com.richard.estoholi.ui.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.richard.estoholi.R
import com.richard.estoholi.ui.calenderView.CalenderActivity
import com.richard.estoholi.ui.holidayList.HolidaytList

open class SharedActivityViews{

     fun setUpBottomBar(startDate : String, context : Activity, bottom_navigation_bar : BottomNavigationBar, isCalenda : Boolean){
            bottom_navigation_bar
                .addItem( BottomNavigationItem(R.drawable.ic_calender_white, "Calenda"))
                .addItem( BottomNavigationItem(R.drawable.ic_listview_white, "List view"))
                .addItem(BottomNavigationItem(R.drawable.ic_settings_white, "Settings"))
                .setFirstSelectedPosition(if(isCalenda) 0 else 1)
                .initialise();


        bottom_navigation_bar.setTabSelectedListener(object :
            BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {
                if(isCalenda){
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
            }

            override fun onTabUnselected(position: Int) {
                Log.d("okh", "Unlicked position $position")
            }

            override fun onTabReselected(position: Int) {
                Log.d("okh", "Reselected position $position")
            }

        })
    }

}