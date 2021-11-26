package com.example.myapplication

import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationSet
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
    //binding追加。これでレイアウトのテキストボックス、ボタンをbinding.textで使える
    private var _binding: FragmentOptionmenuBinding?=null
    private  val binding get()=_binding!!
    //バイブレーションONOFF呼び出し用ファイル
    private var filebaibu: File? = null
    //バイブレーションONまたはOFF格納変数
    var BaibuOr: String? = null
    //画面開かれた時の処理
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
    //メニューがクリックされたときの処理　画面移動
    private inner class ListItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            //行事追加画面
           if(position==0){
               val action=
                   optionmenuDirections.actionOptionmenuToAddoptio()
               findNavController().navigate(action)
            //行事削除画面
           }else if(position==1){
               val action=
                   optionmenuDirections.actionOptionmenuToDeleteOption()
               findNavController().navigate(action)
               //背景変更画面
           }else if(position==2){
               val intent = Intent(context, SampleActivity::class.java)
               val requestCode = 1000
               intent.putExtra(EXTRA_MESSAGE, "a");
               startActivityForResult(intent, requestCode)
               //バイブレーション機能画面
           }else if(position==3){
               val action=
                   optionmenuDirections.actionOptionmenuToBaipretion()
               findNavController().navigate(action)
               //テキストカラー変更
           }else if(position==4){
               val action=
                   optionmenuDirections.actionOptionmenuToTextColorChange()
               findNavController().navigate(action)
               //協力サイト紹介画面
           }else if(position==5){
               val action=
                   optionmenuDirections.actionOptionmenuToSaito()
               findNavController().navigate(action)
           }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //リストがクリックされたときに上にかいてある処理を呼びだす
        binding.list.onItemClickListener = ListItemClickListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //+ボタン非表示
        (activity as? MainActivity) ?.setFabVisible(View.INVISIBLE)
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

    //これわからん。消してもいいかな。とりあえず残しておくわ
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            optionmenu().apply {

            }
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