package com.example.myapplication

import android.graphics.Color
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import java.text.SimpleDateFormat
import java.util.*

class ScheduleAdapter(data: OrderedRealmCollection<Schedule>):
    RealmRecyclerViewAdapter<Schedule, ScheduleAdapter.ViewHolder>(data,true) {
    private var listener: ((Long?)->Unit)?=null
    //現在の日付を取得する。下2行
    val date= getCurrentDateTime()
    val dateInString= date.toString("yyyy/MM/dd")
    fun setOnItemClickListener(listener:(Long?)->Unit){
        this.listener=listener
    }
    fun setOnLongClickListener(listener:(Long?)->Unit){
        this.listener=listener
    }
    init {
        setHasStableIds(true)
    }
    class ViewHolder(cell: View): RecyclerView.ViewHolder(cell){
        //日付専用テキスト作成
        val date: TextView =cell.findViewById(android.R.id.text1)
        //タイトル専用テキスト作成
        val title: TextView =cell.findViewById(android.R.id.text2)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleAdapter.ViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(android.R.layout.simple_list_item_2,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleAdapter.ViewHolder, position: Int) {
        holder.date.setTextSize(25.0F)
        holder.title.setTextSize(20.0F)
        val schedule:Schedule?=getItem(position)
        //時間を省いたyyyy/MM/ddだけを格納
        val Hiduke=DateFormat.format("yyyy/MM/dd",schedule?.date)

        //日付＋時間のデータを格納
        holder.date.text=(DateFormat.format("yyyy/MM/dd HH:mm",schedule?.date)).toString()+">"+(DateFormat.format("HH:mm",schedule?.date2))
        //holder.date.setText(DateFormat.format("yyyy/MM/dd HH:mm",schedule?.date))
        //現在の日付と保存されているデータの比較
        if(Hiduke==dateInString){
            //現在の日付と保存されている日付が同じならbackgroundを赤にする
            holder.date.setBackgroundColor(Color.CYAN)
            holder.title.setBackgroundColor(Color.CYAN)
        }
        holder.title.text=(schedule?.title)+":"+(schedule?.detil)
        holder.itemView.setOnClickListener{
            listener?.invoke(schedule?.id)
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id ?:0
    }
    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter= SimpleDateFormat(format, locale)
        return formatter.format(this)
    }
    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

}