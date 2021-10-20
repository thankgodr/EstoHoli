package com.richard.estoholi.ui.holidayList.adapter

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beardedhen.androidbootstrap.BootstrapLabel
import com.beardedhen.androidbootstrap.BootstrapText
import com.beardedhen.androidbootstrap.BootstrapWell
import com.beardedhen.androidbootstrap.font.FontAwesome
import com.richard.estoholi.R
import com.richard.estoholi.models.Holiday
import com.richard.estoholi.models.HolidayRealm
import io.realm.RealmResults

class HoldayListAdapter: RecyclerView.Adapter<HoldayListAdapter.ViewHolder> {
    private var mdata : Map<String, List<Holiday>?>
    private var  ctx : Context

    constructor(context: Context, data: Map<String, List<Holiday>?>){
        mdata = data
        ctx = context
    }

    constructor(context: Context, holidayRealm: RealmResults<HolidayRealm>){
        mdata  = mutableMapOf()
        holidayRealm.forEach{
            (mdata as MutableMap<String, List<Holiday>?>).put(it.day!!, it.holdays.toList())
        }
        ctx = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.holidayholder,parent,false)
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var key = mdata.keys.toTypedArray()[position]
        holder.txView.bootstrapText = BootstrapText.Builder(ctx).addFontAwesomeIcon(FontAwesome.FA_CALENDAR).addText(  " "+key ).build()
        if(!mdata.get(key).isNullOrEmpty()){
            var gridLayout : RecyclerView.LayoutManager? = null
            if (mdata.get(key)!!.size < 2){
                 gridLayout = LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL,false)
            }
            else{
                 gridLayout = GridLayoutManager(ctx,2, GridLayoutManager.VERTICAL,false)

            }

            holder.recyclerView.layoutManager = gridLayout
            holder.recyclerView.adapter = SingleHolidayAdapter(ctx, mdata.get(key)!!)
        }else{
            holder.recyclerView.visibility = View.GONE
            holder.txtNoEvent.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return mdata.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txView  = itemView.findViewById<BootstrapLabel>(R.id.holdayDate)
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.singleHolday)
        val txtNoEvent = itemView.findViewById<BootstrapWell>(R.id.noevent)
    }

     fun getItems() : Map<String, List<Holiday>?> {
         return mdata
     }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

}