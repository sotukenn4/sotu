package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentSecondBinding
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.where
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    private  var _binding: FragmentSecondBinding?=null
    private  val binding get()=_binding!!
    private lateinit var realm: Realm
    val strList = arrayOf("更新","削除","キャンセル")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm= Realm.getDefaultInstance()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.list.layoutManager= LinearLayoutManager(context)
        var dateTime= Calendar.getInstance().apply {
            timeInMillis=binding.calendarView.date
        }
        findSchedule(
                dateTime.get(Calendar.YEAR),
                dateTime.get(Calendar.MONTH),
                dateTime.get(Calendar.DAY_OF_MONTH)
        )
        binding.calendarView
                .setOnDateChangeListener{ view, year, month, dayOfMonth->
                    findSchedule(year, month, dayOfMonth)
                }
        binding.list.layoutManager= LinearLayoutManager(context)
        val schedules=realm.where<Schedule>().findAll()
        val adapter=ScheduleAdapter(schedules)
        binding.list.adapter=adapter
        adapter.setOnLongClickListener {
            id ->
            id?.let {
                val dialog = ConfirmDialog(
                        "変更しますか？",
                        "削除", { deleteSchedule(view,it)
                    adapter.notifyDataSetChanged()},
                        "更新", {
                    val action=
                            FirstFragmentDirections.actionToScheduleEditFragment(it)
                    findNavController().navigate(action)
                }
                )
                dialog.show(parentFragmentManager, "delete_dialog")
            }
        }
    }
    private fun findSchedule(
            year: Int,
            month: Int,
            dayOfMonth: Int
    ){
        var selectDate=Calendar.getInstance().apply {
            clear()
            set(year, month, dayOfMonth)
        }
        val schedules=realm.where<Schedule>()
                .between(
                        "date",
                        selectDate.time,
                        selectDate.apply {
                            add(Calendar.DAY_OF_MONTH, 1)
                            add(Calendar.MILLISECOND, -1)
                        }.time
                ).findAll().sort("date")
        val adapter=ScheduleAdapter(schedules)
        binding.list.adapter=adapter
        adapter.setOnItemClickListener { id->
            id?.let {
                val action=SecondFragmentDirections.actionToScheduleEditFragment(it)
                findNavController().navigate(action)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
    private fun deleteSchedule(view: View,it:Long){

        realm.executeTransaction{
            db:Realm->db.where<Schedule>().equalTo("id",it)
                ?.findFirst()
                ?.deleteFromRealm()
        }
        Snackbar.make(view, "削除しました", Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.YELLOW)
                .show()


    }
}