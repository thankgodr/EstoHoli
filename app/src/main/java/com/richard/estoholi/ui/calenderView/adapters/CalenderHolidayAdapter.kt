package com.richard.estoholi.ui.calenderView.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.richard.estoholi.models.Holiday

class CalenderHolidayAdapter : RecyclerView.Adapter<CalenderHolidayAdapter.ViewHolder>{

    val mData  : Map<String, List<Holiday>>
    val  ctx  : Context

    constructor(data: Map<String, List<Holiday>>, context: Context){
        mData = data
        ctx = context
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}