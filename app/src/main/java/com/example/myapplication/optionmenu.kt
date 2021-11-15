package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentOptionmenuBinding
import kotlinx.android.synthetic.main.fragment_addoption.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException


class optionmenu : Fragment() {
    private var _binding: FragmentOptionmenuBinding?=null
    private  val binding get()=_binding!!
    //バイブレーションONOFF呼び出し用ファイル
    private var filebaibu: File? = null
    //バイブレーションONまたはOFF格納変数
    var BaibuOr: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    private inner class ListItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
           if(position==0){
               val action=
                   optionmenuDirections.actionOptionmenuToAddoptio()
               findNavController().navigate(action)
           }else if(position==1){
               val action=
                   optionmenuDirections.actionOptionmenuToDeleteOption()
               findNavController().navigate(action)
           }else if(position==2){
               val intent = Intent(context, SampleActivity::class.java)
               val requestCode = 1000
               intent.putExtra(EXTRA_MESSAGE, "a");
               startActivityForResult(intent, requestCode)
           }else if(position==3){
               val action=
                   optionmenuDirections.actionOptionmenuToBaipretion()
               findNavController().navigate(action)
           }else if(position==4){
               val action=
                   optionmenuDirections.actionOptionmenuToTextColorChange()
               findNavController().navigate(action)
           }else if(position==5){
               val action=
                   optionmenuDirections.actionOptionmenuToSaito()
               findNavController().navigate(action)
           }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.onItemClickListener = ListItemClickListener()
        //(activity as? MainActivity<*>) ?.setFabVisible(View.INVISIBLE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding= FragmentOptionmenuBinding.inflate(inflater, container, false)
        val baibu = "BAIP.txt"
        filebaibu = File(requireContext().filesDir, baibu)
        BaibuOr = readBib()
        //バイブレーションがONなら
        if(BaibuOr=="ON"){
            val baip = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            //バイブレーションの長さ　ぶぅぅーん
            baip.vibrate(100)
        }
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            optionmenu().apply {

            }
    }
}