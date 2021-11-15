package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentAddoptionBinding
import com.google.android.material.snackbar.Snackbar
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException


class addoption : Fragment() {
    private var _binding: FragmentAddoptionBinding? = null
    private val binding get() = _binding!!
    private var file: File? = null
    // ファイルを読み出し
    fun readFile(): String? {
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    //保存してあるデータの取り出しメソッド
    private fun getArray(PrefKey: String): Array<String> {
        val GetKey: SharedPreferences =
            requireActivity().getSharedPreferences("Array", Context.MODE_PRIVATE)
        val stringItem = GetKey.getString(PrefKey, "")
        return if (stringItem != null && stringItem.length != 0) {
            stringItem.split(",").toTypedArray()
        } else emptyArray()
    }
    //データの保存メソッド。すまん中身がなにしてるか、説明できるほど理解してない。
    private fun saveArray(array: Array<String>, PrefKey: String) {
        val buffer = StringBuffer()
        var stringItem: String? = null
        for (item in array) {
            buffer.append("$item,")
        }
        val buf = buffer.toString()
        stringItem = buf.substring(0, buf.length - 1)
        val SaveKey: SharedPreferences =
            requireActivity().getSharedPreferences("Array", Context.MODE_PRIVATE)
        val editor = SaveKey.edit()
        editor.putString(PrefKey, stringItem).apply()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddoptionBinding.inflate(inflater, container, false)
        val fileName = "TestFile.txt"
        file = File(requireContext().filesDir, fileName)
        val str: String? = readFile()
        binding.addoptiontitle.setTextColor(Color.parseColor(str));
        binding.addoptiondetail.setTextColor(Color.parseColor(str));
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //+ボタンを非表示
        //(activity as? MainActivity<*>) ?.setFabVisible(View.INVISIBLE)
        //保存データを取り出し、valuesに格納
        var data = getArray("StringItem")
        //保存ボタンがおされたとき
       binding.savebutton.setOnClickListener {
           //追加するのが、空白で何も書かれていない場合は追加しない
           if (binding.addoptiontitleText.text.toString().equals("")) {
               binding.addoptiontitleText.setError("行事名を入力してください")
           } else {
               //check変数。0の時は重複なし。1の時は重複してる
               var check = 0
               //保存されているデータと照らし合わせて追加するデータがすでにないか確認する
               for (i in 1..data.size - 1) {
                   if (data[i].equals(binding.addoptiontitleText.text.toString())) {
                       //重複しているためcheckを1にする
                       check = 1
                       //画面下に黒いラベル表示
                       Snackbar.make(view, "すでに入力された行事はあります", Snackbar.LENGTH_SHORT)
                           .setActionTextColor(Color.YELLOW)
                           .show()
                   }
               }
               //重複があったとき、警告を表示する
               if (check == 1) {
                   /*val dialog = ConfirmDialog("既に入力された項目はありますが追加しますか？",
                           "はい", {
                       //はいが押されたときのメソッド
                       values += binding.spinnertext.text.toString()
                       //テキストの文字は空白に戻す
                       binding.spinnertext.setText("")
                       //追加されたデータを保存する
                       saveArray(values, "StringItem");
                       //画面下に黒いラベル表示
                       Snackbar.make(view, "指定の項目を追加しました", Snackbar.LENGTH_SHORT)
                               .setActionTextColor(Color.YELLOW)
                               .show()
                   },
                           "いいえ", {
                       //いいえが押されたときのメソッド。特になし
                   }
                   )
                   //ダイアログ表示
                   dialog.show(parentFragmentManager, "save_dialog")
                    */
                   //重複がないとき
               } else {
                   //重複がなかったときはそのままデータを追加する
                   data += binding.addoptiontitleText.text.toString()
                   //テキストの文字は空白に戻す
                   binding.addoptiontitleText.setText("")
                   //追加されたデータを保存する
                   saveArray(data, "StringItem");
                   //画面下に黒いラベル表示
                   Snackbar.make(view, "指定の項目を追加しました", Snackbar.LENGTH_SHORT)
                       .setActionTextColor(Color.YELLOW)
                       .show()
               }
           }
       }
    }
}