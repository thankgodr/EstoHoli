package com.richard.estoholi.ui.holidayList

import android.util.Log.d
import com.richard.estoholi.HolidayAplication
import com.richard.estoholi.R
import com.richard.estoholi.models.Holiday
import com.richard.estoholi.ui.helpers.Extension
import com.richard.estoholi.ui.helpers.SimpleUpdate
import com.richard.estoholi.ui.helpers.Utils
import com.richard.estoholi.ui.holidayList.adapter.HoldayListAdapter
import java.text.SimpleDateFormat
import java.util.*

class HolidayPresenter : Extension, HoldayContract.PresenterContract {

    private var contractView : HoldayContract.UiContract
     val appp : HolidayAplication


    constructor(cotractView: HoldayContract.UiContract) : super(cotractView.getContext().applicationContext as HolidayAplication) {
            contractView =  cotractView
        appp=  contractView.getContext().applicationContext as HolidayAplication
    }

    override fun startSearch(startDate: String) {
       val res = getHoliday(startDate)
        if(res!!.size >= 27){
            val adapter = HoldayListAdapter(contractView.getContext(), res)
            contractView.buildAdapters(adapter)
        }else{
            val format = SimpleDateFormat("yyyy-MM-dd")
            val c = Calendar.getInstance()
            c.time = format.parse(startDate)
            c.add(Calendar.DATE, 30)
            val endDate =format.format(c.time)
            contractView.showProgress()
            getData(startDate,endDate, object : SimpleUpdate<Map<String, List<Holiday>?>, String> {
                override fun start() {
                   d("okh", "Stated")
                }

                override fun sucsess(res: Map<String, List<Holiday>?>) {
                    val adapter = HoldayListAdapter(contractView.getContext(),
                        res
                    )
                    if (adapter != null) {
                        contractView.buildAdapters(adapter)
                    }else{
                        contractView.showError(contractView.getContext().getString(R.string.unKnownError))
                    }
                }

                override fun error(err: String) {
                   contractView.showError(err)
                }

                override fun complete() {
                    contractView.hideProgress()
                }

            })
        }
    }

    override fun addDate(startDate: String) {
        val endDtae = Utils.getTheNext30Days(startDate)
        contractView.updateDateUI(startDate, endDtae)
    }

    override fun removeDate(startDate: String) {
        val endDtae = Utils.getPrevious30Days(startDate)
        contractView.updateDateUI(startDate, endDtae)
    }

    override fun upDateDateFromDay(startDate: String, day: Int) {
        val newStartDay = Utils.getNextweekOfDay(day, startDate);
        val endDtae = Utils.getTheNext30Days(startDate)
        contractView.updateDateUI(newStartDay, endDtae)
    }

    override fun showSearchIcon() {
        TODO("Not yet implemented")
    }






}