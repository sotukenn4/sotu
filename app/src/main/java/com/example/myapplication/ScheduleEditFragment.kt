package com.example.myapplication

//import kotlinx.android.synthetic.main.fragment_schedule_edit.*
import android.R
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
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
    //よく使う行事配列
    var values = arrayOf(
            "",
            "旅行",
            "面接",
            "バイト",
            "病院",
            "学校"
    )
    //spinnerの数カウント
    var i=0
    //画面開かれたとき
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()




    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentScheduleEditBinding.inflate(inflater, container, false)
        val adapter = ArrayAdapter(requireActivity(), R.layout.simple_spinner_item, values)
        adapter.setDropDownViewResource(R.layout.simple_dropdown_item_1line)
        binding.spinner.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //このViewに来た時、更新ボタンを押してきたら
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

        },
                "キャンセル", {
            Snackbar.make(it, "キャンセルしました", Snackbar.LENGTH_SHORT)
                    .show()
        }
        )
            dialog.show(parentFragmentManager, "save_dialog")}

        //spinnerの処理
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
                    //選択されたアイテムをタイトルテキストにコピー
                    item?.let {
                        if (it.isNotEmpty()) binding.titleEdit.setText(item)
                    }
                }
                //何も選択されなかったとき。特に処理はないのでメソッドの中は空でよし
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }}
        //日付選択ボタンが押されたときにカレンダーダイアログ表示
        binding.datebutton.setOnClickListener{
            DateDialog{ date->
                binding.dateEdit.setText(date)

            }.show(parentFragmentManager, "date_dialog")}
        //時間選択ボタンが押されたときに時間ダイアログ表示
        binding.timeButton.setOnClickListener{
            TimeDialog{ time->
                binding.timeEdit.setText(time)
            }.show(parentFragmentManager, "time_dialog")
        }
    }
    private fun saveSchedule(view: View){
        when (args.scheduleId){
            //-1Lのとき、つまり新規のとき
            -1L -> {
                realm.executeTransaction { db: Realm ->
                    val maxId = db.where<Schedule>().max("id")
                    val nextId = (maxId?.toLong() ?: 0L) + 1L
                    val schedule = db.createObject<Schedule>(nextId)
                    val date = "${binding.dateEdit.text} ${binding.timeEdit.text}".toDate()
                    if (date != null) schedule.date = date
                    schedule.title = binding.titleEdit.text.toString()
                    schedule.detil = binding.detailEdit.text.toString()
                }

                //タイトルが空でなくspinnerに一つも同じのがなければspinnerの項目に追加
                if (!(binding.titleEdit.text.toString().equals(""))) {
                    for (str in values) {
                        if (!(binding.titleEdit.text.toString().equals(str))) {
                            i++
                            if (i >= values.size) {
                                values += binding.titleEdit.text.toString()
                                val adapter = ArrayAdapter(requireActivity(), R.layout.simple_spinner_item, values)
                                adapter.setDropDownViewResource(R.layout.simple_dropdown_item_1line)
                                binding.spinner.adapter = adapter
                                i = 0
                            }

                        }
                    }
                }

                //黒いバー表示（追加）
                Snackbar.make(view, "追加しました", Snackbar.LENGTH_SHORT)
                        .setAction("戻る") { findNavController().popBackStack() }
                        .setActionTextColor(Color.YELLOW)
                        .show()

                //新規でなく更新のとき
            } else->{
            realm.executeTransaction { db: Realm ->
                val schedule = db.where<Schedule>().equalTo("id", args.scheduleId).findFirst()
                val date=("${binding.dateEdit.text}"+"${binding.timeEdit.text}").toDate()
                if(date!=null) schedule?.date=date
                schedule?.title=binding.titleEdit.text.toString()
                schedule?.detil=binding.detailEdit.text.toString()
            }
            //黒いバー（更新）
            Snackbar.make(view, "修正しました", Snackbar.LENGTH_SHORT)
                .setAction("戻る"){findNavController().popBackStack()}
                .setActionTextColor(Color.YELLOW)
                .show()
        }}}
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