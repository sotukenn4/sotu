package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentFirstBinding
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class KakoYotei: Fragment() {
    private var _binding: FragmentFirstBinding?=null
    private  val binding get()=_binding!!
    private  lateinit var realm: Realm
    val date= getCurrentDateTime()
    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm= Realm.getDefaultInstance()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity) ?.setFabVisible(View.INVISIBLE)
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.DATE,-1)
        val date1: Date = cal.time
        //ここから4行でリストにデータをセットして表示する
        binding.list.layoutManager= LinearLayoutManager(context)
        //ralm、adapterの設定
        var schedules=realm.where<Schedule>().lessThan("date",date1).findAll()
        schedules = schedules.sort("date", Sort.DESCENDING);
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }


}