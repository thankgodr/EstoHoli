package com.richard.estoholi.ui.holidayList

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.beardedhen.androidbootstrap.BootstrapText
import com.beardedhen.androidbootstrap.TypefaceProvider
import com.richard.estoholi.R
import com.richard.estoholi.ui.helpers.Utils
import com.richard.estoholi.ui.holidayList.adapter.HoldayListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat
import java.util.*

class HolidaytList : AppCompatActivity(), HoldayContract.UiContract, View.OnClickListener {
    lateinit var presenter: HolidayPresenter
    lateinit var startDate: String

    lateinit var progresBar : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TypefaceProvider.registerDefaultIconSets()
        setContentView(R.layout.activity_main)
        progresBar = ProgressDialog(this)
        attachPresenter()
        setUPUI()

    }

    private fun attachPresenter() {
        presenter = HolidayPresenter(this)
    }

    private fun setUPUI(){
        //Setup change listerner for firday selecttion
        firstDaySelect.setOnDropDownItemClickListener { parent, v, id -> run{
            val selected = this@HolidaytList.resources.getStringArray(R.array.bootstrap_dropdown_example_data)[id]
            firstDaySelect.bootstrapText = BootstrapText.Builder(this@HolidaytList).addText(selected).build()
            firstDaySelect.setTextColor(this.resources.getColor(R.color.white))
            presenter.upDateDateFromDay(startDate, id)
        } }


        //Setup firstTIme to show todays date
       startDate = Utils.getTodaysDate()


        //Listerners
        btnRightArrow.setOnClickListener(this)
        btnLeftArrow.setOnClickListener(this)


    }


    override fun showProgress() {
        if(progresBar == null){
            progresBar = ProgressDialog(this)
        }
       progresBar.setTitle("Loading...")
        progresBar.show()
    }

    override fun hideProgress() {
        progresBar.hide()
    }

    override fun buildAdapters(adapter : HoldayListAdapter) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

    }

    override fun showError(errorMessage: String) {
        alert{
            isCancelable = false
            title = this.ctx.getString(R.string.errorTitle)
            message = errorMessage
            positiveButton(this.ctx.getString(R.string.closeString), {
                it.cancel()
            })
        }.show()
    }

    override fun updateDateUI(vStartDate: String, endDate: String) {
        dateHolder.text = endDate+ " - " + vStartDate
        startDate = endDate

        //Lets start Search to update
        presenter.startSearch(startDate)

    }

    override fun getContext(): Context {
        return this
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.btnRightArrow -> presenter.addDate(startDate)

            R.id.btnLeftArrow -> presenter.removeDate(startDate)

        }
    }
}