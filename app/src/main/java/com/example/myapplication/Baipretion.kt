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
    private var _binding: FragmentBaipretionBinding?=null
    private  val binding get()=_binding!!
    private var swith:Int=0
    private var file: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentBaipretionBinding.inflate(inflater, container, false)
        binding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            swith = if (isChecked) {
                0
            } else {
                1
            }
            if(swith==0){
                saveFile("ON")
                binding.textView10.text="現在:ON"
                binding.textView10.setTextColor(Color.BLACK)
            }else{
                saveFile("OFF")
                binding.textView10.text="現在:OFF"
                binding.textView10.setTextColor(Color.GRAY)
            }
        }
        binding.textView10.setOnClickListener {
            if(binding.textView10.text=="現在:OFF"){
                binding.textView10.text="現在:"+"ON"
                binding.textView10.setTextColor(Color.BLACK)
                binding.switch1.isChecked=true
            }else{
                binding.textView10.text="現在:OFF"
                binding.textView10.setTextColor(Color.GRAY)
                binding.switch1.isChecked=false
            }
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as? MainActivity<*>) ?.setFabVisible(View.INVISIBLE)
        val fileName = "BAIP.txt"
        file = File(requireContext().filesDir, fileName)
        val check: String? = readFile()
        binding.textView10.text="現在:"+check.toString()
        if(check.equals("OFF")){
            binding.textView10.setTextColor(Color.GRAY)
            binding.switch1.isChecked=false
        }else{
            binding.textView10.setTextColor(Color.BLACK)
            binding.switch1.isChecked=true
        }


    }
    // ファイルを保存
    fun saveFile(str: String?) {
        // try-with-resources
        try {
            FileWriter(file).use { writer -> writer.write(str) }
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
        return text
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
    override fun onDestroy() {
        super.onDestroy()

    }
}