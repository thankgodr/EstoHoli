package com.richard.estoholi.ui.holidayList.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beardedhen.androidbootstrap.AwesomeTextView
import com.richard.estoholi.R
import com.richard.estoholi.models.Holiday

class SingleHolidayAdapter: RecyclerView.Adapter<SingleHolidayAdapter.ViewHolder> {

    private var context : Context
    private var mData : List<Holiday>

    constructor(ctx : Context, data : List<Holiday>){
        context = ctx
        mData = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.singleholdaylayout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.setText(mData.get(position).name)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<AwesomeTextView>(R.id.name)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}