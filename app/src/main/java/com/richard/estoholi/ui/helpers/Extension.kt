package com.richard.estoholi.ui.helpers

import android.util.Log
import com.google.gson.Gson
import com.richard.estoholi.HolidayAplication
import com.richard.estoholi.R
import com.richard.estoholi.models.Holiday
import com.richard.estoholi.models.HolidayRealm
import com.richard.estoholi.models.HolidayRequest
import com.richard.estoholi.models.HolidayResponse
import com.richard.estoholi.networking.NeworkRequest
import com.richard.estoholi.ui.holidayList.adapter.HoldayListAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.RealmResults
import io.realm.Sort
import java.text.SimpleDateFormat
import java.util.*

open class Extension(val  app : HolidayAplication) {
    protected fun getHoliday(startDate: String): RealmResults<HolidayRealm>? {
        //Add 30 days to the start date
        val format = SimpleDateFormat("yyyy-MM-dd")
        val c = Calendar.getInstance()
        c.time = format.parse(startDate)
        c.add(Calendar.DATE, 30)
        val endDate =format.format(c.time)
        Log.d("okh", startDate + " " + endDate)
        //Start request
        val res = getDataDB(startDate,endDate)
        return res
    }

    protected fun getDataDB(startDate: String, endDate: String): RealmResults<HolidayRealm>? {
        val results = app.realm.where(HolidayRealm::class.java)
            .between("dateTime", Utils.parseDate(startDate), Utils.parseDate(endDate))
            .sort("dateTime", Sort.ASCENDING)
            .findAll()
        return results
    }


    protected fun getData(startDate : String,endDate: String, simpleUpdate: SimpleUpdate< Map<String, List<Holiday>?>,String>){
        val postBody = Gson().toJson(HolidayRequest(startDate,endDate))
        simpleUpdate.start()
        NeworkRequest<HolidayResponse>(HolidayResponse())
            .post(app.getString(R.string.baseUrl),postBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                simpleUpdate.complete()
                val adapter = HoldayListAdapter(app,
                    populateEMptyDate(startDate,endDate,it)
                )
                if (adapter != null) {
                    simpleUpdate.sucsess(populateEMptyDate(startDate,endDate,it))
                }else{
                    simpleUpdate.error(app.getString(R.string.unKnownError))
                }

                saveNewResponace(startDate,endDate,it)
            },{
                simpleUpdate.complete()
                if (it.message.isNullOrEmpty()) simpleUpdate.error(it.localizedMessage) else simpleUpdate.error(
                    it.message!!
                )
            })
    }

    private  fun saveNewResponace(startDate: String, endDtae: String,holidayResponse: HolidayResponse){
        val newMap = populateEMptyDate(startDate,endDtae, holidayResponse)
        newMap.forEach{
            if(!it.key.isNullOrEmpty()){
                app.realm.beginTransaction();
                val holidayRealm = app.realm.createObject(HolidayRealm::class.java, it.key)
                holidayRealm.setDate()
                if(!it.value.isNullOrEmpty()){
                    it.value!!.forEach {
                        val singleHoliday =  app.realm.copyToRealm(it)
                        holidayRealm.holdays!!.add(singleHoliday)
                    }
                }
                app.realm.commitTransaction()
            }
        }
    }


    private fun populateEMptyDate(startDate: String, endDtae :String, response: HolidayResponse): Map<String, List<Holiday>?> {
        val mapp = mutableMapOf<String, List<Holiday>?>()
        if(response.holidays?.containsKey(startDate) !== true){
            mapp[startDate] =  null
        }
        val format = SimpleDateFormat("yyyy-MM-dd")
        val c = Calendar.getInstance()
        c.time = format.parse(startDate)
        c.add(Calendar.DATE, 1)
        var nextDate = format.format(c.time)
        while(nextDate != endDtae){
            if(response.holidays?.containsKey(nextDate) !== true){
                mapp[nextDate] =  null
            }else{
                mapp[nextDate] = response.holidays!!.get(nextDate)
            }
            c.add(Calendar.DATE, 1)
            nextDate = format.format(c.time)
        }


        return mapp.toMap()


    }

}