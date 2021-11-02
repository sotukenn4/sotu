package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView


class ListViewAdapter internal constructor(
    context: Array<String>, itemLayoutId: Int,
    scenes: Array<String>
) : BaseAdapter() {
    internal class ViewHolder {
        var textView: TextView? = null

    }

    private val inflater: LayoutInflater = TODO()
    private val itemLayoutId: Int = 0
    private val titles: Array<String>
    private val ids: IntArray
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView: View? = convertView
        val holder: ViewHolder
        // 最初だけ View を inflate して、それを再利用する
        if (convertView == null) {
            // activity_main.xml に list.xml を inflate して convertView とする
            convertView = inflater.inflate(itemLayoutId, parent, false)
            // ViewHolder を生成
            holder = ViewHolder()
            //holder.textView = convertView.findViewById(R.id.textView)
            convertView.setTag(holder)
        } else {
            holder = convertView.getTag() as ViewHolder
        }


        // 現在の position にあるファイル名リストを holder の textView にセット
        holder.textView!!.text = titles[position]
        return convertView
    }

    override fun getCount(): Int {
        // texts 配列の要素数
        return titles.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    init {
       /* inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.itemLayoutId = itemLayoutId
        titles = scenes
        ids = photos*/
    }
}