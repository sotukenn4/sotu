package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentBaipretionBinding
import com.example.myapplication.databinding.FragmentFirstBinding
import java.io.*


class Baipretion : Fragment() {
    //binding追加。これでレイアウトのテキストボックス、ボタンをbinding.textで使える
    private var _binding: FragmentBaipretionBinding?=null
    private  val binding get()=_binding!!
    //バイブのON・OFFのため使う変数
    private var swith:Int=0
    private var file: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentBaipretionBinding.inflate(inflater, container, false)
        //スイッチ処理
        binding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            swith = if (isChecked) { 0 } else { 1 }
            //ONにする
            if(swith==0){
                saveFile("ON")
                binding.BigButtonText.text="現在:ON"
                binding.BigButtonText.setTextColor(Color.BLACK)
                //OFFにする
            }else{
                saveFile("OFF")
                binding.BigButtonText.text="現在:OFF"
                binding.BigButtonText.setTextColor(Color.GRAY)
            }
        }

        //本当はスイッチでON・OFF切り替えてほしいけど、人間は大きいボタンが切り替えボタンだと思い、押すと思うで、優しい俺はこの機能を着けてあげる
        binding.BigButtonText.setOnClickListener {
            if(binding.BigButtonText.text=="現在:OFF"){
                binding.BigButtonText.text="現在:"+"ON"
                binding.BigButtonText.setTextColor(Color.BLACK)
                binding.switch1.isChecked=true
            }else{
                binding.BigButtonText.text="現在:OFF"
                binding.BigButtonText.setTextColor(Color.GRAY)
                binding.switch1.isChecked=false
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //現在のON/OFF呼びだす
        val fileName = "BAIP.txt"
        file = File(requireContext().filesDir, fileName)
        val check: String? = readFile()
        //checkには今現在のON・OFFどっちかが入ってる
        binding.BigButtonText.text="現在:"+check.toString()
        if(check.equals("OFF")){
            binding.BigButtonText.setTextColor(Color.GRAY)
            binding.switch1.isChecked=false
        }else{
            binding.BigButtonText.setTextColor(Color.BLACK)
            binding.switch1.isChecked=true
        }
    }

    // ファイルを保存
    fun saveFile(str: String?) {
        // try-with-resources
        try {
            FileWriter(file).use { writer -> writer.write(str) } } catch (e: IOException) { e.printStackTrace() }
    }
    // ファイルを読み出し
    fun readFile(): String? {
        var text: String? = null
        // try-with-resources
        try {
            BufferedReader(FileReader(file)).use { br -> text = br.readLine() } } catch (e: IOException) { e.printStackTrace() }
        return text
    }

    //画面から離れたときの処理　した二つ
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
    override fun onDestroy() {
        super.onDestroy()

    }
}