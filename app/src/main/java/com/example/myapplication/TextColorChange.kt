package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentTextColorChangeBinding
import com.google.android.material.snackbar.Snackbar
import java.io.*

class TextColorChange : Fragment() {
    private var _binding: FragmentTextColorChangeBinding?=null
    private  val binding get()=_binding!!
    private var file: File? = null
    private lateinit var color :String

    private inner class ListItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            //黒
            if(position==0){
                color="#000000"
                saveFile(color)
            //白
            }else if(position==1){
                color="#FFFFFF"
                saveFile(color)
            //赤
            }else if(position==2){
                color="#FF0000"
                saveFile(color)
            //青
            }else if(position==3){
                color="#0000FF"
                saveFile(color)
            //緑
            }else if(position==4){
                color="#008000"
                saveFile(color)
            //黄
            }else if(position==5){
                color="#FFFF00"
                saveFile(color)
            //茶
            }else if(position==6){
                color="#B22222"
                saveFile(color)
            //紫
            }else if(position==7){
                color="#FF00FF"
                saveFile(color)
            //オレンジ
            }else if(position==8){
                color="#FFA500"
                saveFile(color)
            }
            //画面下に黒いラベル表示
            if (view != null) {
                Snackbar.make(view, "指定の色に変更しました", Snackbar.LENGTH_SHORT)
                    .setActionTextColor(Color.YELLOW)
                    .show()
            }
        }
    }
    // ファイルを保存
    fun saveFile(str: String?) {
        // try-with-resources
        try {
            FileWriter(file).use { writer -> writer.write(str) }
            binding.textcolor.setTextColor(Color.parseColor(str))
        } catch (e: IOException) {
            e.printStackTrace()
        }
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listcolor.onItemClickListener = ListItemClickListener()
        (activity as? MainActivity<*>) ?.setFabVisible(View.INVISIBLE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding= FragmentTextColorChangeBinding.inflate(inflater, container, false)
        val fileName = "TestFile.txt"
        file = File(requireContext().filesDir, fileName)
        val str: String? = readFile()
        binding.textcolor.setTextColor(Color.parseColor(str))
        return binding.root
    }


}