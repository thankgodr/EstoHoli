package com.richard.estoholi.ui.calenderView.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.richard.estoholi.R
import com.richard.estoholi.models.Holiday
import com.richard.estoholi.ui.helpers.Utils
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class CalenderHolidayAdapter : RecyclerView.Adapter<CalenderHolidayAdapter.ViewHolder>{

    val mData  : List<Holiday>
    val  ctx  : Context
    val dateString : Date
    val localDate : LocalDate

    constructor(date: Date, data: List<Holiday>, context: Context){
        mData = data
        ctx = context
        dateString = date
        localDate = convertToLocalDateViaInstant(date)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val  holiName = itemView.findViewById<TextView>(R.id.HoliName)
        val startDateTv = itemView.findViewById<TextView>(R.id.startDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.calenda_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.startDateTv.text = Utils.getHumanShort(localDate)
        holder.holiName.text = mData.get(position).name
        if(position % 2 == 0){
            holder.startDateTv.setBackgroundColor(ctx.resources.getColor(R.color.date2))
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }


    private fun convertToLocalDateViaInstant(dateToConvert: Date): LocalDate {
        return dateToConvert.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}