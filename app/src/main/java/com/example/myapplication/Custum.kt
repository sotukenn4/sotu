package com.example.myapplication

import android.R
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentCustumBinding
import com.google.android.material.snackbar.Snackbar


//オプション画面のプログラム
class Custum : Fragment() {
    val EXTRA_MESSAGE
            = "YourPackageName.MESSAGE"
    //bindingの定義。これはどこでも使う。
    private var _binding: FragmentCustumBinding?=null
    private val binding get()=_binding!!
    private var _binding2: MainActivity?=null
    private val binding2 get()=_binding2!!
    private var word:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    //保存してあるデータの取り出しメソッド
    private fun getArray(PrefKey: String): Array<String> {
        val GetKey: SharedPreferences =  requireActivity().getSharedPreferences("Array", Context.MODE_PRIVATE)
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
        if (buffer != null) {
            val buf = buffer.toString()
            stringItem = buf.substring(0, buf.length - 1)
            val SaveKey: SharedPreferences = requireActivity().getSharedPreferences("Array", Context.MODE_PRIVATE)
            val editor = SaveKey.edit()
            editor.putString(PrefKey, stringItem).commit()
        }
    }
//Viewが作られたときに呼びだされる
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding= FragmentCustumBinding.inflate(inflater,container,false)
    // OnItemSelectedListenerの実装
    binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

        // 項目が選択された時に呼ばれる
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
           word = parent?.selectedItem as String

        }

        // 基本的には呼ばれないが、何らかの理由で選択されることなく項目が閉じられたら呼ばれる
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
    }
    /*
    binding.button2.setOnClickListener(View.OnClickListener {
        val intent = Intent(context, SampleActivity::class.java)
        intent.putExtra("EXTRA_DATA",100);
        startActivity(intent)
    })
*/

    binding.button.setOnClickListener(View.OnClickListener {
        val intent = Intent(context, SampleActivity::class.java)
        val requestCode = 1000
        intent.putExtra(EXTRA_MESSAGE,"a");
        startActivityForResult(intent, requestCode)
    })
        return binding.root
    }

fun spinnerset(data: Array<String>){
    //spinnerに配列valuesの値をいれる。3行セット
    val adapter = ArrayAdapter(requireActivity(), R.layout.simple_spinner_item, data)
    adapter.setDropDownViewResource(R.layout.simple_dropdown_item_1line)
    binding.spinner2.adapter = adapter
}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //保存データを取り出し、valuesに格納
        var data = getArray("StringItem")
        //予備の配列。0番目に空白
        var YobiHairetu = arrayOf(" ")
       /* //spinnerに配列valuesの値をいれる。3行セット
        val adapter = ArrayAdapter(requireActivity(), R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(R.layout.simple_dropdown_item_1line)
        binding.spinner2.adapter = adapter*/
        spinnerset(data)

        //追加ボタンが押された時のメソッド
        binding.spinneradd.setOnClickListener {
            //追加するのが、空白で何も書かれていない場合は追加しない
            if (binding.spinnertext.text.toString().equals("")) {
                binding.spinnertext.setError("行事名を入力してください")
            } else {
                //check変数。0の時は重複なし。1の時は重複してる
                var check = 0
                //保存されているデータと照らし合わせて追加するデータがすでにないか確認する
                for (i in 1..data.size - 1) {
                    if (data[i].equals(binding.spinnertext.text.toString())) {
                        //重複しているためcheckを1にする
                        check = 1
                        //画面下に黒いラベル表示
                        Snackbar.make(view, "すでに入力された行事はあります", Snackbar.LENGTH_SHORT)
                            .setActionTextColor(Color.YELLOW)
                            .show()
                    } else {
                        //特に処理はない
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
                    data += binding.spinnertext.text.toString()
                    //テキストの文字は空白に戻す
                    binding.spinnertext.setText("")
                    //追加されたデータを保存する
                    saveArray(data, "StringItem");
                    //画面下に黒いラベル表示
                    Snackbar.make(view, "指定の項目を追加しました", Snackbar.LENGTH_SHORT)
                            .setActionTextColor(Color.YELLOW)
                            .show()
                    //spinner更新
                    spinnerset(data)
                }
            }
        }
        //削除ボタンが押された時の処理
        binding.spinnerdelete.setOnClickListener {
            //削除するのが、空白で何も書かれていない場合は処理しない
            if (word.equals(" ")||word==null) {
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




