package com.example.myapplication

import android.R
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentFirstBinding
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.where
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException


class FirstFragment : Fragment() ,View.OnCreateContextMenuListener {
    private var _binding: FragmentFirstBinding?=null
    private  val binding get()=_binding!!
    private  lateinit var realm: Realm
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
        //(activity as? MainActivity<*>) ?.setFabVisible(View.VISIBLE)
        //ここから4行でリストにデータをセットして表示する
        binding.list.layoutManager= LinearLayoutManager(context)
        //ralm、adapterの設定
        val schedules=realm.where<Schedule>().findAll()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
    //データ削除メソッド
    private fun deleteSchedule(view: View,it:Long){
        realm.executeTransaction{
        db:Realm->db.where<Schedule>().equalTo("id",it)
            ?.findFirst()
            ?.deleteFromRealm()
        }
        //画面下に黒いラベル表示
        Snackbar.make(view, "削除しました", Snackbar.LENGTH_SHORT)
            .setActionTextColor(Color.YELLOW)
            .show()
    }

}



