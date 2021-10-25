package com.richard.estoholi.ui.holidayList

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.beardedhen.androidbootstrap.BootstrapText
import com.beardedhen.androidbootstrap.TypefaceProvider
import com.richard.estoholi.R
import com.richard.estoholi.ui.helpers.ChangeDay
import com.richard.estoholi.ui.helpers.CollapserAnim
import com.richard.estoholi.ui.helpers.SharedActivityViews
import com.richard.estoholi.ui.helpers.Utils
import com.richard.estoholi.ui.holidayList.adapter.HoldayListAdapter
import io.realm.internal.Util
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*

class HolidaytList : AppCompatActivity(), HoldayContract.UiContract, View.OnClickListener {
    lateinit var presenter: HolidayPresenter
    lateinit var startDate: String
    lateinit var genEndDate : String

    lateinit var progresBar : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TypefaceProvider.registerDefaultIconSets()
        setContentView(R.layout.activity_main)
        progresBar = ProgressDialog(this)
        attachPresenter()
        supportActionBar!!.hide()
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
            firstDaySelect.setTextColor(this.resources.getColor(R.color.black))
            presenter.upDateDateFromDay(startDate, id)
        } }


        //Setup firstTIme to show todays date
       startDate = Utils.getTodaysDate()
        genEndDate = Utils.getTheNext30Days(startDate)

        updateDateUI(startDate, genEndDate)

        //Listerners
        btnRightArrow.setOnClickListener(this)
        btnLeftArrow.setOnClickListener(this)
        showMenu.setOnClickListener(this)
        showMenu.setColorFilter(Color.RED)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
               if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
                   if(topSearch.visibility == View.VISIBLE){
                       CollapserAnim.collapse(topSearch)
                       CollapserAnim.expand(showMenu)
                    }
                   if(!recyclerView.canScrollVertically(-1)){
                       CollapserAnim.collapse(showMenu)
                       CollapserAnim.expand(topSearch)
                   }
               }
            }
        })


        //Set up Buttom bar
        SharedActivityViews(false).setUpBottomBar(startDate, this, bottom_navigation_bar,  object :
            ChangeDay {
            override fun updateNewFirstDay(week: DayOfWeek) {
                d("okh", "Selected is ${week}")
            }

        })
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
        d("okh",  endDate+ " - " + vStartDate);
        dateHolder.text = vStartDate+ " - " + endDate
        startDate = vStartDate
        genEndDate = endDate

        //Lets start Search to update
        presenter.startSearch(vStartDate)

    }

    override fun getContext(): Context {
        return this
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.btnRightArrow -> presenter.addDate(genEndDate)

            R.id.btnLeftArrow -> presenter.removeDate(genEndDate)

            R.id.showMenu -> {
                CollapserAnim.expand(topSearch)
                CollapserAnim.collapse(showMenu)
            }


        }
    }
}