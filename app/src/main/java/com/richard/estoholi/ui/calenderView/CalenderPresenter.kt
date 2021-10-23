package com.richard.estoholi.ui.calenderView

import android.util.Log.d
import com.richard.estoholi.HolidayAplication
import com.richard.estoholi.models.Holiday
import com.richard.estoholi.ui.helpers.Extension
import com.richard.estoholi.ui.helpers.SimpleUpdate
import com.richard.estoholi.ui.holidayList.adapter.SingleHolidayAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalenderPresenter : Extension, CalenderContract.presenter {

    val contractview : CalenderContract.UI

    constructor(view : CalenderContract.UI) : super(view.getContext().applicationContext as HolidayAplication) {
        contractview = view
    }

    override fun getHolidays(startDate: String) {
      val res =  getHoliday(startDate)
        if (res != null) {
            if(res.size >= 27){
                val listHol =  mutableMapOf<String, List<Holiday>>()
                res.forEach({
                    listHol.put(it.day!!, it.holdays)
                })
                contractview.returnedHoliday(listHol)
            }else{
                contractview.showPregress()

                val format = SimpleDateFormat("yyyy-MM-dd")
                val c = Calendar.getInstance()
                c.time = format.parse(startDate)
                c.add(Calendar.DATE, 30)
                val endDate =format.format(c.time)
                getData(startDate, endDate, object :
                    SimpleUpdate<Map<String, List<Holiday>?>, String> {
                    override fun start() {
                       contractview.showPregress()
                    }

                    override fun sucsess(res: Map<String, List<Holiday>?>) {
                        contractview.hideProgress()
                        contractview.returnedHoliday(res as Map<String, List<Holiday>>)
                    }

                    override fun error(err: String) {
                        contractview.showError(err)
                    }

                    override fun complete() {
                        contractview.hideProgress()
                    }

                })
            }
        }
    }




}