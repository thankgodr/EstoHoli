package com.richard.estoholi.ui.holidayList.adapter

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
import com.richard.estoholi.ui.helpers.CollapserAnim
import com.richard.estoholi.ui.helpers.Utils
import io.realm.RealmResults
import io.realm.internal.Util

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
        holder.txDayNAme.text = Utils.getDayIt(key)
        if(!mdata.get(key).isNullOrEmpty()){
            holder.txtNotext.setText(ctx.resources.getString(R.string.totalHoldayis).replace("$$", mdata.get(key)!!.size.toString()))
            var gridLayout = LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL,false)
            holder.recyclerView.layoutManager = gridLayout
            holder.recyclerView.adapter = SingleHolidayAdapter(ctx, mdata.get(key)!!)
        }else{
            holder.recyclerView.visibility = View.GONE
            holder.wellNoEvent.visibility = View.VISIBLE
        }

        holder.divHolder.setOnClickListener({
            if(holder.recyclerView.visibility == View.VISIBLE){
                CollapserAnim.collapse(holder.recyclerView)
            }else{
                CollapserAnim.expand(holder.recyclerView)
            }
        })
    }

    override fun getItemCount(): Int {
        return mdata.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txView  = itemView.findViewById<BootstrapLabel>(R.id.holdayDate)
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.singleHolday)
        val wellNoEvent = itemView.findViewById<BootstrapWell>(R.id.noevent)
        val txtNotext = itemView.findViewById<TextView>(R.id.noeventText)
        val divHolder = itemView.findViewById<LinearLayout>(R.id.divHolder)

        val txDayNAme = itemView.findViewById<TextView>(R.id.dayName)
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

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        CollapserAnim.collapse(holder.recyclerView)
        super.onViewDetachedFromWindow(holder)
    }

}