package com.example.myapplication

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/*class TabAdapter(fm: FragmentManager, private val context: Context): FragmentPagerAdapter(fm){

    val _flagmentTodoInComplete = TodaySchedule(context)
    val _flagmentTodoComplete = FirstFragment(context)
    val _flagmentTodoAll = KakoYotei(context)

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> { return _flagmentTodoInComplete }
            1 -> {return _flagmentTodoComplete}
            else ->  { return _flagmentTodoAll }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> { return context.getString(R.string.Baip); }
            1 -> { return context.getString(R.string.Baip); }
            else ->  { return context.getString(R.string.Baip); }
        }
    }

    override fun getCount(): Int {
        return 3
    }
}*/