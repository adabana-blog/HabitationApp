package com.websarva.wings.android.HabitationApp

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.calender_items.view.*

class CalendarItemListAdapter(context: Context,items:List<CalendarItems>):ArrayAdapter<CalendarItems>(context,0,items) {
    data class ViewHolder(val date:TextView,val dayOfWeek:TextView,val check:ImageView,val emoticon:ImageView)
    private val mInflater:LayoutInflater

    init {
        this.mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view:View
        val holder:ViewHolder

        if (convertView==null){
            view = mInflater.inflate(R.layout.calender_items,parent,false)
            holder = ViewHolder(view.tvDate,view.tvDayOfWeek,view.ivCheckBox,view.ivEmoticon)
            view.tag =holder
        }
        else{
            view = convertView
            holder = view.tag as ViewHolder
        }

        val calendarItem = getItem(position) as CalendarItems
        holder.date.text = calendarItem.date
        holder.dayOfWeek.text = calendarItem.dayOfWeek
        //holder.check.setImageBitmap(BitmapFactory.decodeResource(context.resources,calendar.checkId))
        //holder.emoticon.setImageBitmap(BitmapFactory.decodeResource(context.resources,calendar.emoticonId))
        return view
    }
}