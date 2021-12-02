package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentBaipretionBinding
import com.example.myapplication.databinding.FragmentToiawaseBinding
import com.google.android.material.snackbar.Snackbar
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Toiawase.newInstance] factory method to
 * create an instance of this fragment.
 */
class Toiawase : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    //binding追加。これでレイアウトのテキストボックス、ボタンをbinding.textで使える
    private var _binding: FragmentToiawaseBinding?=null
    private  val binding get()=_binding!!
    //バイブレーションONOFF呼び出し用ファイル
    private var filebaibu: File? = null
    //バイブレーションONまたはOFF格納変数
    var BaibuOr: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //+ボタンを非表示にする。MainActivityのメソッドを呼びだしている
        (activity as? MainActivity) ?.setFabVisible(View.INVISIBLE)
        binding.sousin.setOnClickListener {
            if("${binding.editTextTextPersonName2.text}"!=""){
                if("${binding.editTextTextPersonName2.text}"=="${binding.editTextTextPersonName3.text}"){
                    view?.let { it1 ->
                        Snackbar.make(it1, "送信しました", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                    binding.editTextTextPersonName.setText("")
                    binding.editTextTextPersonName2.setText("")
                    binding.editTextTextPersonName3.setText("")
                }else{
                    view?.let { it1 ->
                        Snackbar.make(it1, "メールアドレスが一致しません。", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                    binding.editTextTextPersonName2.setError("")
                    binding.editTextTextPersonName3.setError("")
                }
            }else{
                view?.let { it1 ->
                    Snackbar.make(it1, "問い合わせ欄を入力してください", Snackbar.LENGTH_SHORT)
                        .show()
                }
                binding.editTextTextPersonName.setError("")
            }


        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentToiawaseBinding.inflate(inflater, container, false)
        //BAIPにON・OFF
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Toiawase.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Toiawase().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}