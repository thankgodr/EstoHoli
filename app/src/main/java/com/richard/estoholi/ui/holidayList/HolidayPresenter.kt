package com.richard.estoholi.ui.holidayList

import android.util.Log.d
import com.google.gson.Gson
import com.richard.estoholi.HolidayAplication
import com.richard.estoholi.R
import com.richard.estoholi.models.Holiday
import com.richard.estoholi.models.HolidayRealm
import com.richard.estoholi.models.HolidayRequest
import com.richard.estoholi.models.HolidayResponse
import com.richard.estoholi.networking.NeworkRequest
import com.richard.estoholi.ui.helpers.Utils
import com.richard.estoholi.ui.holidayList.adapter.HoldayListAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.RealmList
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import okio.Utf8
import java.text.SimpleDateFormat
import java.util.*

class HolidayPresenter : HoldayContract.PresenterContract {

    private var contractView : HoldayContract.UiContract
    val app : HolidayAplication


    constructor(cotractView: HoldayContract.UiContract){
            contractView =  cotractView
       app=  contractView.getContext().applicationContext as HolidayAplication
    }

    override fun startSearch(startDate: String) {
        getHoliday(startDate)
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


    private fun getHoliday(startDate: String){
        //Add 30 days to the start date
        val format = SimpleDateFormat("yyyy-MM-dd")
        val c = Calendar.getInstance()
        c.time = format.parse(startDate)
        c.add(Calendar.DATE, 30)
        val endDate =format.format(c.time)
        d("okh", startDate + " " + endDate)
        //Start request
        val res = getDataDB(startDate,endDate)
        d("dbdb", res!!.asJSON())
        if(res!!.size >= 27){
            val adapter = HoldayListAdapter(contractView.getContext(), res)
            contractView.buildAdapters(adapter)
        }else{
            contractView.showProgress()
            getData(startDate,endDate)
        }
    }

    private fun getDataDB(startDate: String, endDate: String): RealmResults<HolidayRealm>? {
        val results = app.realm.where(HolidayRealm::class.java)
            .between("dateTime", Utils.parseDate(startDate), Utils.parseDate(endDate))
                .sort("dateTime", Sort.ASCENDING)
            .findAll()
      return results
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




    private fun getData(startDate : String,endDate: String){
        val postBody = Gson().toJson(HolidayRequest(startDate,endDate))
        NeworkRequest<HolidayResponse>(HolidayResponse())
            .post(contractView.getContext().getString(R.string.baseUrl),postBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                contractView.hideProgress()
                val adapter = HoldayListAdapter(contractView.getContext(),
                    populateEMptyDate(startDate,endDate,it)
                )
                if (adapter != null) {
                    contractView.buildAdapters(adapter)
                }else{
                    contractView.showError(contractView.getContext().getString(R.string.unKnownError))
                }

                saveNewResponace(startDate,endDate,it)
            },{
                contractView.hideProgress()
                if (it.message.isNullOrEmpty()) contractView.showError(it.localizedMessage) else contractView.showError(
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

}