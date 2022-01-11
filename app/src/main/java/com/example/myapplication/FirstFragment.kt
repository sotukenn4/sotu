package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentFirstBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class FirstFragment: Fragment() ,View.OnCreateContextMenuListener {
    private var _binding: FragmentFirstBinding?=null
    private  val binding get()=_binding!!
    val date= getCurrentDateTime()
    private val dateInString= date.toString("yyyy/MM/dd")
    private  lateinit var realm: Realm
    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter= SimpleDateFormat(format, locale)
        return formatter.format(this)
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
    //バイブレーションONOFF呼び出し用ファイル
    private var filebaibu: File? = null
    //バイブレーションONまたはOFF格納変数
    var BaibuOr: String? = null
    //配列
    val strList = arrayOf("更新","削除","キャンセル")
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

        when(item.itemId){
            //占いボタンがおされたとき
            R.id.menu_uranei ->{
                val intent = Intent(requireContext(), MainActivity2::class.java)
                startActivity(intent)
            }
            //天気ボタンが押されたとき
            R.id.menu_wheather ->{
                val intent = Intent(requireContext(), tenki::class.java)
                startActivity(intent)

            }
        }
        return true
    }

    // ファイルを読み出し バイブレーションON・OFF
    fun readBib(): String? {
        var text: String? = null
        // try-with-resources
        try { BufferedReader(FileReader(filebaibu)).use { br -> text = br.readLine() } } catch (e: IOException) { e.printStackTrace() }
        return text
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentFirstBinding.inflate(inflater, container, false)
        //BAIPにON・OFF
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
        var cal = Calendar.getInstance()
        /*
        cal.time = Date()
        //cal.add(Calendar.DATE,-1)
        val date1: Date = cal.time
        //ここから4行でリストにデータをセットして表示する
        binding.list.layoutManager= LinearLayoutManager(context)
        //ralm、adapterの設定
        var schedules=realm.where<Schedule>().greaterThanOrEqualTo("date",date1).findAll()
        schedules = schedules.sort("date");
        val adapter=ScheduleAdapter(schedules)
        binding.list.adapter=adapter*/
        var cal2 = Calendar.getInstance()
        cal2.time = Date()
        cal2.add(Calendar.DATE,+1)
        var date4: Date = cal2.time
        var date5= date4.toString("yyyy/MM/dd")
        var date6 = stringToDate(date5,"yyyy/MM/dd")
        //ここから4行でリストにデータをセットして表示する
        binding.list.layoutManager= LinearLayoutManager(context)
        //ralm、adapterの設定
        var schedules=realm.where<Schedule>().between("date",date,date6).findAll()
        //できた――――――――――――ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー
        //感動ｄ－ーーーーーー＾＾＾＾＾＾＾＾＾＾＾＾＾＾＾＾＾～～～～～～～～～～～～～～～～
        schedules = schedules.sort("date", Sort.ASCENDING);
        var adapter=ScheduleAdapter(schedules)
        binding.list.adapter=adapter
        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            // タブが選択された際に呼ばれる
            override fun onTabSelected(tab: TabLayout.Tab) {
               if(tab.text?.equals("今日の予定") == true){
                   cal2 = Calendar.getInstance()
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
                   adapter=ScheduleAdapter(schedules)
                   binding.list.adapter=adapter
               }else if(tab.text?.equals("明日以降の予定") == true){
                   cal = Calendar.getInstance()
                   cal.time = Date()
                   cal.add(Calendar.DATE,+1)
                   val date1: Date = cal2.time
                   val date2= date1.toString("yyyy/MM/dd")
                   val date3 = stringToDate(date2,"yyyy/MM/dd")
                   //ここから4行でリストにデータをセットして表示する
                   binding.list.layoutManager= LinearLayoutManager(context)
                   //ralm、adapterの設定
                   var schedules=realm.where<Schedule>().greaterThan("date",date3).findAll()
                   schedules = schedules.sort("date", Sort.ASCENDING);
                   adapter=ScheduleAdapter(schedules)
                   binding.list.adapter=adapter
               }else{
                   cal = Calendar.getInstance()
                   cal.time = Date()
                   //cal.add(Calendar.DATE,-1)
                   val date1: Date = cal.time
                   //ここから4行でリストにデータをセットして表示する
                   binding.list.layoutManager= LinearLayoutManager(context)
                   //ralm、adapterの設定
                   var schedules=realm.where<Schedule>().lessThan("date",date1).findAll()
                   schedules = schedules.sort("date", Sort.DESCENDING);
                   adapter=ScheduleAdapter(schedules)
                   binding.list.adapter=adapter
               }
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

            // タブが未選択になった際に呼ばれる
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            // 同じタブが選択された際に呼ばれる
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
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
//画面から離れたとき
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
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
    //データ削除メソッド
    private fun deleteScheduleAuTo(view: View, it: Date){
        realm.executeTransaction{
                db:Realm->db.where<Schedule>().equalTo("date",it)
            ?.findFirst()
            ?.deleteFromRealm()
        }

    }
}



