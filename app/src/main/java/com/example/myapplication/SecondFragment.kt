package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.view.*
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentOptionmenuBinding
import com.example.myapplication.databinding.FragmentSecondBinding
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.where
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    private  var _binding: FragmentSecondBinding?=null
    private  val binding get()=_binding!!
    //バイブレーションONOFF呼び出し用ファイル
    private var filebaibu: File? = null
    //バイブレーションONまたはOFF格納変数
    var BaibuOr: String? = null
    private lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm= Realm.getDefaultInstance()
        //バーに占いのチェックボタン表示
        setHasOptionsMenu(true)
    }
    //itemをバーに適用
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater){
        inflater.inflate(com.example.myapplication.R.menu.uranaimenu,menu)
    }
    //占いボタンが押された時の処理
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //バイブレーションがONになっている場合
        if(BaibuOr=="ON"){
            val baip = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            //バイプの長さを100ミリ秒
            baip.vibrate(100)
        }
        //占いボタンがおされたとき
        when(item.itemId){
            //com.example.myapplication.R.id.menu_uranei ->
        }
        return true
    }
    // ファイルを読み出し バイブレーションON・OFF
    fun readBib(): String? {
        var text: String? = null
        // try-with-resources
        try {
            BufferedReader(FileReader(filebaibu)).use { br -> text = br.readLine() }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return text
    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentSecondBinding.inflate(inflater, container, false)
        val baibu = "BAIP.txt"
        filebaibu = File(requireContext().filesDir, baibu)
        BaibuOr = readBib()
        //バイブレーションがONなら
        if(BaibuOr=="ON"){
            val baip = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            //バイブレーションの長さ　ぶぅぅーん
            baip.vibrate(100)
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity) ?.setFabVisible(View.VISIBLE)
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
        //データがタッチされたら
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