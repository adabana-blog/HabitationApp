package com.websarva.wings.android.HabitationApp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.daily_check_habits.view.*

class DailyHabitListAdapter(context: Context,items:List<DailyHabitItems>):ArrayAdapter<DailyHabitItems>(context,0,items) {

    data class ViewHolder(val name:TextView,val checkBox:CheckBox,val emoticon:ImageView)

    private val mInflater: LayoutInflater

    init {
        this.mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view:View
        val holder:ViewHolder

        if(convertView == null){
            view = mInflater.inflate(R.layout.daily_check_habits,parent,false)
            holder = ViewHolder(
                view.tvHabitName,
                view.checkBox,
                view.ivEmoticon
            )
            view.tag = holder
        }
        else{
            view = convertView
            holder = view.tag as ViewHolder
        }

        val dailyHabitItem = getItem(position) as DailyHabitItems
        holder.name.text =dailyHabitItem.name

        when(dailyHabitItem.checkId){
            1 -> holder.checkBox.isChecked = true
            else -> holder.checkBox.isChecked = false
        }
        //holder.emoticon.setImageBitmap(BitmapFactory.decodeResource(context.resources,calendar.emoticonId))
        return view
    }
}