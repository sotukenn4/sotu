package com.example.myapplication

import android.R
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentDeleteOptionBinding
import com.google.android.material.snackbar.Snackbar
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException


class DeleteOption : Fragment() {
    //bindingの定義。これはどこでも使う。
    private var _binding:FragmentDeleteOptionBinding? = null
    private val binding get() = _binding!!
    private var file: File? = null
    //spinnerで選択されたアイテムを格納する変数word
    private var word: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
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
    ): View? {
        //binding扱うために必要。これがないと処理落ちする
        _binding = FragmentDeleteOptionBinding.inflate(inflater, container, false)
        // OnItemSelectedListenerの実装
        //spinnerがクリックされたときの処理メソッド
        val fileName = "TestFile.txt"
        file = File(requireContext().filesDir, fileName)
        val str: String? = readFile()
        binding.deleteoptiontitle.setTextColor(Color.parseColor(str));
        binding.deleteoptiondetail.setTextColor(Color.parseColor(str));
        binding.deleteoptionspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            // 項目が選択された時に呼ばれる
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //変数に選択されたものを格納
                word = parent?.selectedItem as String
            }
            // 基本的には呼ばれないが、何らかの理由で選択されることなく項目が閉じられたら呼ばれる
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        return binding.root
    }
    fun spinnerset(data: Array<String>) {
        //spinnerに配列valuesの値をいれる。3行セット
        val adapter = ArrayAdapter(requireActivity(), R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(R.layout.simple_dropdown_item_1line)
        binding.deleteoptionspinner.adapter = adapter
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //+ボタンを非表示
        //(activity as? MainActivity<*>) ?.setFabVisible(View.INVISIBLE)
        //保存データを取り出し、valuesに格納
        var data = getArray("StringItem")
        //予備の配列。0番目に空白
        var YobiHairetu = arrayOf(" ")
        //spinner更新
        spinnerset(data)
        //削除ボタンが押された時の処理
        binding.deleteoptionbutton.setOnClickListener {
            //削除するのが、空白で何も書かれていない場合は処理しない
            if (word.equals(" ") || word == null) {
                //binding.spinnerOption2.setError("行事名を入力してください")
            } else {
                //削除するデータをのぞいたデータを予備の配列value2に格納する
                for (i in 1..data.size - 1) {
                    if (data[i].equals(word)) {
                        //削除データは格納しないから何も書かない
                    } else {
                        //予備の配列に格納
                        YobiHairetu += data[i].toString()
                    }
                }
                //予備の配列のデータを格納
                data = YobiHairetu
                //データを保存する
                saveArray(data, "StringItem")
                //画面下に黒いラベル表示
                Snackbar.make(view, "指定の項目の削除完了しました", Snackbar.LENGTH_SHORT)
                    .setActionTextColor(Color.YELLOW)
                    .show()
                //spinner更新
                spinnerset(YobiHairetu)

            }

        }
    }

}