package com.richard.estoholi.ui.calenderView

import com.richard.estoholi.HolidayAplication
import com.richard.estoholi.models.Holiday
import com.richard.estoholi.ui.helpers.Extension
import com.richard.estoholi.ui.helpers.SimpleUpdate
import java.text.SimpleDateFormat
import java.util.*

class CalenderPresenter : Extension, CalenderContract.presenter {

    val contractview : CalenderContract.UI

    constructor(view : CalenderContract.UI) : super(view.getContext().applicationContext as HolidayAplication) {
        contractview = view
    }

    override fun getHolidays(startDate: String) {
      val res =  getHoliday(startDate)
        if (res != null) {
            if(res.size >= 27){
                contractview.returnedHoliday()
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
                        TODO("Not yet implemented")
                    }

                    override fun sucsess(res: Map<String, List<Holiday>?>) {
                        TODO("Not yet implemented")
                    }

                    override fun error(err: String) {
                        TODO("Not yet implemented")
                    }

                    override fun complete() {
                        TODO("Not yet implemented")
                    }

                })
            }
        }
    }




}