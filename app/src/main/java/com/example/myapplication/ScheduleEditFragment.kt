package com.example.myapplication

import android.R
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.databinding.FragmentScheduleEditBinding
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_schedule_edit.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



/**
 * A simple [Fragment] subclass.
 * Use the [ScheduleEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScheduleEditFragment: Fragment() {

    private var _binding: FragmentScheduleEditBinding?=null
    private val binding get()=_binding!!
    private lateinit var realm: Realm
    private  val args: ScheduleEditFragmentArgs by navArgs()
    var values = arrayOf(
        "",
        "旅行",
        "面接",
        "バイト",
        "病院",
        "学校"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()

        values= getArray("StringItem")



    }

    private fun getArray(PrefKey: String): Array<String> {
        val prefs2: SharedPreferences =  requireActivity().getSharedPreferences("Array", Context.MODE_PRIVATE)
        val stringItem = prefs2.getString(PrefKey, "d")
        return if (stringItem != null && stringItem.length != 0) {
            stringItem.split(",").toTypedArray()
        } else emptyArray()
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentScheduleEditBinding.inflate(inflater, container, false)


        values= getArray("StringItem")
        val adapter = ArrayAdapter(requireActivity(), R.layout.simple_spinner_item, values)
        adapter.setDropDownViewResource(R.layout.simple_dropdown_item_1line)
        binding.spinner.adapter = adapter
        return binding.root
    }
    private fun saveArray(array: Array<String>, PrefKey: String) {
        val buffer = StringBuffer()
        var stringItem: String? = null
        for (item in array) {
            buffer.append("$item,")
        }
        if (buffer != null) {
            val buf = buffer.toString()
            stringItem = buf.substring(0, buf.length - 1)
            val prefs1: SharedPreferences =  requireActivity().getSharedPreferences("Array", Context.MODE_PRIVATE)
            val editor = prefs1.edit()
            editor.putString(PrefKey, stringItem).commit()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(args.scheduleId !=-1L) {
            val schedule = realm.where<Schedule>()
                .equalTo("id", args.scheduleId).findFirst()
            binding.dateEdit.setText(DateFormat.format("yyyy/MM/dd", schedule?.date))
            binding.timeEdit.setText(DateFormat.format("HH:mm", schedule?.date))
            binding.titleEdit.setText(schedule?.title)
            binding.detailEdit.setText(schedule?.detil)

        }else{

        }

        binding.save.setOnClickListener{val dialog=ConfirmDialog("保存しますか？",
            "保存", {
                saveSchedule(it)
                saveArray(values,"StringItem");
            },
            "キャンセル", {
                Snackbar.make(it, "キャンセルしました", Snackbar.LENGTH_SHORT)
                    .show()
            }
        )
            dialog.show(parentFragmentManager, "save_dialog")}
        binding.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val spinner = parent as? Spinner
                    val item = spinner?.selectedItem as? String

                    item?.let {
                        if (it.isNotEmpty()) binding.titleEdit.setText(item)



                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }}

        binding.datebutton.setOnClickListener{
            DateDialog{ date->
                binding.dateEdit.setText(date)

            }.show(parentFragmentManager, "date_dialog")}
        binding.timeButton.setOnClickListener{
            TimeDialog{ time->
                binding.timeEdit.setText(time)
            }.show(parentFragmentManager, "time_dialog")
        }

    }
    private fun saveSchedule(view:View){
        when (args.scheduleId){
            -1L -> {
        realm.executeTransaction{db:Realm->
            val maxId=db.where<Schedule>().max("id")
            val nextId=(maxId?.toLong() ?:0L)+1L
            val schedule=db.createObject<Schedule>(nextId)
            val date="${binding.dateEdit.text} ${binding.timeEdit.text}".toDate()
            if (date != null) schedule.date = date
            schedule.title = binding.titleEdit.text.toString()
            schedule.detil = binding.detailEdit.text.toString()
        }
                saveArray(values,"StringItem")
        Snackbar.make(view, "追加しました", Snackbar.LENGTH_SHORT)
            .setAction("戻る") { findNavController().popBackStack() }
            .setActionTextColor(Color.YELLOW)
            .show()

    } else->{
            realm.executeTransaction { db: Realm ->
                val schedule = db.where<Schedule>().equalTo("id", args.scheduleId).findFirst()
                val date=("${binding.dateEdit.text}"+"${binding.timeEdit.text}").toDate()
                if(date!=null) schedule?.date=date
                schedule?.title=binding.titleEdit.text.toString()
                schedule?.detil=binding.detailEdit.text.toString()
            }
            Snackbar.make(view, "修正しました", Snackbar.LENGTH_SHORT)
                .setAction("戻る"){findNavController().popBackStack()}
                .setActionTextColor(Color.YELLOW)
                .show()
        }}}
    private fun deleteSchedule(view: View){
        realm.executeTransaction{ db: Realm->
            db.where<Schedule>().equalTo("id", args.scheduleId)
                ?.findFirst()
                ?.deleteFromRealm()

        }
        Snackbar.make(view, "削除しました", Snackbar.LENGTH_SHORT)
            .setActionTextColor(Color.YELLOW)
            .show()
        findNavController()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
    private fun String.toDate(pattern: String = "yyyy/MM/ddHH:mm"): Date?{
        return try{
            SimpleDateFormat(pattern).parse(this)
        }catch (e: IllegalArgumentException){
            return  null
        }catch (e: ParseException){
            return null
        }
    }

}