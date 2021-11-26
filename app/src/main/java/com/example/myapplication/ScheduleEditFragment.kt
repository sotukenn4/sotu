package com.example.myapplication

import android.R
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.text.format.DateFormat
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.databinding.FragmentScheduleEditBinding
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_schedule_edit.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ScheduleEditFragment: Fragment() {
    //bindingの定義。これはどこでも使う。
    private var _binding: FragmentScheduleEditBinding?=null
    private val binding get()=_binding!!
    val action= ScheduleEditFragmentDirections.actionScheduleEditFragmentToFirstFragment()
    //テキストカラー呼び出し用ファイル
    private var file: File? = null
    //バイブレーションONOFF呼び出し用ファイル
    private var filebaibu: File? = null
    //バイブレーションONまたはOFF格納変数
    var BaibuOr: String? = null
    //realm（保存のデータベース？みたいなもの）
    private lateinit var realm: Realm
    //これはよくわからん。
    private  val args: ScheduleEditFragmentArgs by navArgs()
    //配列定義。中身は仮設定
    var values = arrayOf("", "旅行", "面接", "バイト", "病院", "学校")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
        //保存されている新しいデータにする
        values= getArray("StringItem")
        //バーに保存のチェックボタン表示
        setHasOptionsMenu(true)
    }
    //itemをバーに適用
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater){
        inflater.inflate(com.example.myapplication.R.menu.save,menu)
    }
    //保存ボタンが押された時の処理
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //バイブレーションがONになっている場合
        if(BaibuOr=="ON"){
            val baip = requireActivity().getSystemService(VIBRATOR_SERVICE) as Vibrator
            //バイプの長さを100ミリ秒
            baip.vibrate(100)
        }
        //保存ボタンがおされたとき
        when(item.itemId){
            com.example.myapplication.R.id.menu_save ->
                savemethod()
            //戻るボタンが押されたとき一覧画面に遷移
            R.id.home ->
            findNavController().navigate(action)

        }
        return true
    }

    // ファイルを読み出し　カラーコード
    fun readColor(): String? {
        var text: String? = null
        // try-with-resources
        try {
            BufferedReader(FileReader(file)).use { br -> text = br.readLine() }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //テキストカラーが登録されていなけば黒色指定（これがないと新規端末でアプリが落ちる）
        if(text==null){
            text="#000000"
        }
        return text
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
    //保存してあるデータの取り出しメソッド　一括処理
    private fun getArray(PrefKey: String): Array<String> {
        val prefs2: SharedPreferences =  requireActivity().getSharedPreferences("Array", Context.MODE_PRIVATE)
        val stringItem = prefs2.getString(PrefKey, "")
        return if (stringItem != null && stringItem.length != 0) {
            stringItem.split(",").toTypedArray()
        } else emptyArray()
    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding= FragmentScheduleEditBinding.inflate(inflater, container, false)
        //TestFileにカラー情報あるから
        val fileName = "TestFile.txt"
        //TestFileから情報を取り出して
        file = File(requireContext().filesDir, fileName)
        val colorcode: String? = readColor()
        //BAIPにON・OFF
        val baibu = "BAIP.txt"
        filebaibu = File(requireContext().filesDir, baibu)
        BaibuOr = readBib()
        //バイブレーションがONなら
        if(BaibuOr=="ON"){
            val baip = requireActivity().getSystemService(VIBRATOR_SERVICE) as Vibrator
            //バイブレーションの長さ　ぶぅぅーん
            baip.vibrate(100)
        }
        //「日付」
        binding.textView2.setTextColor(Color.parseColor(colorcode))
        //「開始時間」
        binding.textView3.setTextColor(Color.parseColor(colorcode))
        //「タイトル」
        binding.textView4.setTextColor(Color.parseColor(colorcode))
        //「詳細」
        binding.textView5.setTextColor(Color.parseColor(colorcode))
        //「終了時間」
        binding.finishText.setTextColor(Color.parseColor(colorcode))
        //保存されている新しいデータにする
        values= getArray("StringItem")
        //spinnerに配列valuesの値をいれる。3行セット
        val adapter = ArrayAdapter(requireActivity(), R.layout.simple_spinner_item, values)
        adapter.setDropDownViewResource(R.layout.simple_dropdown_item_1line)
        binding.spinner.adapter = adapter
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //+ボタンを非表示にする。MainActivityのメソッドを呼びだしている
        (activity as? MainActivity) ?.setFabVisible(View.INVISIBLE)
        //更新ボタンが押されてこの画面に来た時。更新用
        if(args.scheduleId !=-1L) {
            //idからデータを読み込む
            val schedule = realm.where<Schedule>()
                .equalTo("id", args.scheduleId).findFirst()
            //yyyy/MM/ddの形でテキストに日にちをセット
            binding.dateEdit.setText(DateFormat.format("yyyy/MM/dd", schedule?.date))
            //HH:mmの形で時間をセット
            binding.timeEdit.setText(DateFormat.format("HH:mm", schedule?.date))
            //終了時間が設定されているデータならテキストにセット
            if(schedule?.timeflg==0){
                binding.timeEdit2.setText(DateFormat.format("HH:mm", schedule.date2))
            }
            //タイトルをセット
            binding.titleEdit.setText(schedule?.title)
            //詳細をセット
            binding.detailEdit.setText(schedule?.detil)
        }
        //spinnerが押された時の処理
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
                        if(it.equals(values[0])){
                            //spinnerの一番上の空白が選択された場合は何もしない
                        }else{
                            //空白以外のアイテムが選択されたときに左のテキストにセットする
                            if (it.isNotEmpty()) binding.titleEdit.setText(item)
                        }
                    }
                }
                //何も押されなかった時の処理
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }}
        //カレンダーボタン押されたらカレンダー表示する
        binding.datebutton.setOnClickListener{
            DateDialog{ date->
                binding.dateEdit.setText(date)
            }.show(parentFragmentManager, "date_dialog")}
        //時間ボタン(開始)
        binding.timeButton.setOnClickListener{
            TimeDialog{ time->
                binding.timeEdit.setText(time)
            }.show(parentFragmentManager, "time_dialog")
        }
        //時間ボタン(終了)
        binding.timeButton2.setOnClickListener{
            TimeDialog{ time->
                binding.timeEdit2.setText(time)
            }.show(parentFragmentManager, "time_dialog")
        }

    }
    //保存ボタンが押されたときに表示するダイアログ
    @RequiresApi(Build.VERSION_CODES.O)
    fun savemethod(){
        val dialog=ConfirmDialog("保存しますか？(日時が正しく入力されていないと現在の日時で保存されます)",
            "保存", {
                //保存が押された時
                view?.let { saveSchedule(it) }
            },
            //キャンセルが押されたとき
            "キャンセル", {
            }
        )
        dialog.show(parentFragmentManager, "save_dialog")
    }
    //保存ボタンが押されたときの処理
    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveSchedule(view:View){
        //日付と時間の形式チェック　toDateメソッド
        if(("${binding.dateEdit.text}"+"${binding.timeEdit.text}").toDate()!=null){
            //タイトルチェック　何も入力されていないとエラー
            if("${binding.titleEdit.text}"!=""){
                when (args.scheduleId){
                    //idが-1L、つまり新規で作成されてるなら
                    -1L -> {
                        realm.executeTransaction{db:Realm->
                            //新しいidを作り変数に格納
                            val maxId=db.where<Schedule>().max("id")
                            //idを更新しておく？一個上の処理で作ったidの次のidを次回から使えるようにするため
                            val nextId=(maxId?.toLong() ?:0L)+1L
                            //nextIdを保存しておく
                            val schedule=db.createObject<Schedule>(nextId)
                            //データ日付格納　開始時間
                            val date="${binding.dateEdit.text} ${binding.timeEdit.text}".toDate()
                            //日付保存
                            if (date != null) schedule.date = date
                            //終了時間　形式確認後格納
                            val date2="${binding.dateEdit.text} ${binding.timeEdit2.text}".toDate()
                            //形式が正しければ保存
                            if (date2 != null) {
                                schedule.date2 = date2
                                //アダプタークラスでtimeflgによって終了時間が表示されるかはんだんされる
                                schedule.timeflg =0
                            }else{
                                schedule.timeflg =1
                            }
                            //タイトルを保存
                            schedule.title = binding.titleEdit.text.toString()
                            //詳細を保存
                            schedule.detil = binding.detailEdit.text.toString()
                        }
                        //saveArray(values,"StringItem")　多分これいらん
                        //if(check==0){
                        //画面下に黒いラベル表示
                        Snackbar.make(view, "追加しました", Snackbar.LENGTH_SHORT)
                            .setAction("戻る") { findNavController().popBackStack() }
                            .setActionTextColor(Color.YELLOW)
                            .show()
                        // }
                        //idが-1L以外だった時。つまり更新目的で来た時
                    } else->{
                    //realm呼び出し
                    realm.executeTransaction { db: Realm ->
                        //すでにあるidをいれる。更新だから
                        val schedule = db.where<Schedule>().equalTo("id", args.scheduleId).findFirst()
                        //データ日付格納
                        val date=("${binding.dateEdit.text}"+"${binding.timeEdit.text}").toDate()
                        //日付保存
                        if(date!=null) schedule?.date=date
                        val date2=("${binding.dateEdit.text}"+"${binding.timeEdit2.text}").toDate()
                        if (date2 != null)
                            schedule?.date2 = date2
                        //タイトルを保存
                        schedule?.title=binding.titleEdit.text.toString()
                        //詳細を保存
                        schedule?.detil=binding.detailEdit.text.toString()
                    }
                    //if(check==0){
                    //画面下に黒いラベル表示
                    Snackbar.make(view, "修正しました", Snackbar.LENGTH_SHORT)
                        .setAction("戻る"){findNavController().popBackStack()}
                        .setActionTextColor(Color.YELLOW)
                        .show()
                    // }
                    }
                }
            }else{
                //タイトルが未入力ならエラー表示
                binding.textView4.setError("タイトルを入力してください")
            }
        }else{
            if("${binding.titleEdit.text}"==""){
                //タイトルが未入力ならエラー表示
                binding.textView4.setError("タイトルを入力してください")
                //日時の入力形式が違う、または未入力の場合エラー表示
                binding.textView2.setError("日時エラー")
                //日時の入力形式が違う。終了時間は任意入力のため未入力でもエラーはでない
                binding.textView3.setError("日時エラー")
            }else{
                //日時の入力形式が違う、または未入力の場合エラー表示
                binding.textView2.setError("日時エラー")
                //日時の入力形式が違う。終了時間は任意入力のため未入力でもエラーはでない
                binding.textView3.setError("日時エラー")
            }
        }
    }
    //onDestroyViewとonDestoryはセットで書いておいて。意味はわからんけど
    //10/08の時点では、これはこの画面離れた時におきる処理だと思う
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
    //日付取得
    private fun String.toDate(pattern: String = "yyyy/MM/ddHH:mm"): Date?{
        return try{
            SimpleDateFormat(pattern).parse(this)
        }catch (e: IllegalArgumentException){
            return  null
        }catch (e: ParseException){
            //日付形式が違う場合
            /*val builder: AlertDialog.Builder = AlertDialog.Builder(view?.context)
            builder.setTitle("エラー")
            builder.setMessage("日付または時間の入力形式が違います。")
            builder.setPositiveButton("確認", null)
            builder.create().show()
            check=1*/
            return null
        }
    }

}