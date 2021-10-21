package com.richard.estoholi.ui.holidayList

import android.content.Context
import com.richard.estoholi.ui.holidayList.adapter.HoldayListAdapter

interface HoldayContract {
    interface UiContract{
        fun showProgress()
        fun hideProgress()
        fun buildAdapters(adapter : HoldayListAdapter)
        fun showError(errorMessage : String)
        fun updateDateUI(startDate: String, endDate: String)
        fun getContext() : Context
    }

    interface PresenterContract{
        fun startSearch(startDate : String)
        fun addDate(startDate: String)
        fun removeDate(startDate: String)
        fun upDateDateFromDay(startDate: String, day: Int)
        fun showSearchIcon()
    }
}