package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentTodayScheduleBinding
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class TodaySchedule: Fragment() {
    private var _binding: FragmentTodayScheduleBinding?=null
    private  val binding get()=_binding!!
    private  lateinit var realm: Realm
    val date= getCurrentDateTime()
    private val dateInString= date.toString("yyyy/MM/dd")
    private val dateInString2= dateInString + "00"
    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter= SimpleDateFormat(format, locale)
        return formatter.format(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm= Realm.getDefaultInstance()
    }
    fun stringToDate(dateStr: String?, format: String?): Date? {
        val sdf = SimpleDateFormat(format, Locale.JAPAN)
        var date: Date? = null
        try {
            date = sdf.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentTodayScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity) ?.setFabVisible(View.INVISIBLE)
        val hiduke = stringToDate(dateInString,"yyyy/MM/dd")
        binding.date.text=dateInString
        val cal = Calendar.getInstance()
        cal.time = Date()
        //cal.add(Calendar.DATE,-1)
        val date1: Date = cal.time
         val date2= date1.toString("yyyy/MM/dd")
        val date3 = stringToDate(date2,"yyyy/MM/dd")
        val cal2 = Calendar.getInstance()
        cal2.time = Date()
        cal2.add(Calendar.DATE,+1)
        val date4: Date = cal2.time
        val date5= date4.toString("yyyy/MM/dd")
        val date6 = stringToDate(date5,"yyyy/MM/dd")
        //ここから4行でリストにデータをセットして表示する
        binding.list.layoutManager= LinearLayoutManager(context)
        //ralm、adapterの設定
        var schedules=realm.where<Schedule>().between("date",date,date6).findAll()
        //できた――――――――――――ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー
        //感動ｄ－ーーーーーー＾＾＾＾＾＾＾＾＾＾＾＾＾＾＾＾＾～～～～～～～～～～～～～～～～
        schedules = schedules.sort("date", Sort.ASCENDING);
        val adapter=ScheduleAdapter(schedules)
        binding.list.adapter=adapter
        //タッチされた時の処理
        adapter.setOnLongClickListener {
                id ->
            id?.let {
                //ダイアログ表示
                val dialog = ConfirmDialog(
                    "変更しますか？",
                    //削除の場合
                    "削除", {
                        //データを消すメソッド呼び出し（deleteSchedule）
                        deleteSchedule(view,it)
                        //リストを更新する。
                        adapter.notifyDataSetChanged()},
                    //更新の場合
                    "更新", {
                        //選択されたデータのidをitに入れてscheduleedit画面に遷移する
                        val action=
                            FirstFragmentDirections.actionToScheduleEditFragment(it)
                        findNavController().navigate(action)
                    }
                )
                //ダイアログ表示
                dialog.show(parentFragmentManager, "delete_dialog")
            }
        }
    }
    //データ削除メソッド
    private fun deleteSchedule(view: View, it: Long){
        realm.executeTransaction{
                db:Realm->db.where<Schedule>().equalTo("id",it)
            ?.findFirst()
            ?.deleteFromRealm()
        }
        //画面下に黒いラベル表示
        Snackbar.make(view, "削除しました", Snackbar.LENGTH_SHORT).setActionTextColor(Color.YELLOW)
            .show()
    }

}