package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentCustumBinding
import com.google.android.material.snackbar.Snackbar


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Custum.newInstance] factory method to
 * create an instance of this fragment.
 */
class Custum : Fragment() {
    private var _binding: FragmentCustumBinding?=null
    private val binding get()=_binding!!
    var values = arrayOf(
        "",
        "旅行",
        "面接",
        "バイト",
        "病院",
        "学校"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }
    private fun getArray(PrefKey: String): Array<String> {
        val prefs2: SharedPreferences =  requireActivity().getSharedPreferences("Array", Context.MODE_PRIVATE)
        val stringItem = prefs2.getString(PrefKey, "")
        return if (stringItem != null && stringItem.length != 0) {
            stringItem.split(",").toTypedArray()
        } else emptyArray()
    }
    private fun saveArray(array: Array<String>, PrefKey: String) {
        val buffer = StringBuffer()
        var stringItem: String? = null
        for (item in array) {
            buffer.append("$item,")
        }
        if (buffer != null) {
            val buf = buffer.toString()
            stringItem = buf.substring(0, buf.length - 1)
            val prefs1: SharedPreferences = requireActivity().getSharedPreferences("Array", Context.MODE_PRIVATE)
            val editor = prefs1.edit()
            editor.putString(PrefKey, stringItem).commit()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding= FragmentCustumBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var values = getArray("StringItem")
        var value2= arrayOf(" ")
        binding.spinneradd.setOnClickListener{
            if(binding.spinnertext.text.toString().equals("")){
                Snackbar.make(view, "追加したい行事の入力をしてください", Snackbar.LENGTH_SHORT)
                        .setActionTextColor(Color.YELLOW)
                        .show()
            }else{
                var check=0
                for ( i in 1 .. values.size-1){
                    if(values[i].equals(binding.spinnertext.text.toString())){
                        check=1
                    }else{

                    }
                }
                if(check==1){
                    val dialog=ConfirmDialog("既に入力された項目はありますが追加しますか？",
                            "はい", {
                        values+=binding.spinnertext.text.toString()
                        binding.spinnertext.setText("")
                        saveArray(values,"StringItem");
                        Snackbar.make(view, "指定の項目を追加しました", Snackbar.LENGTH_SHORT)
                                .setActionTextColor(Color.YELLOW)
                                .show()
                    },
                            "いいえ", {

                    }
                    )
                    dialog.show(parentFragmentManager, "save_dialog")
                }else{
                    values+=binding.spinnertext.text.toString()
                    binding.spinnertext.setText("")
                    saveArray(values,"StringItem");
                    Snackbar.make(view, "指定の項目を追加しました", Snackbar.LENGTH_SHORT)
                            .setActionTextColor(Color.YELLOW)
                            .show()
                }
            }


        }
        binding.spinnerdelete.setOnClickListener{
            if(binding.spinnertext2.text.toString().equals("")){
                Snackbar.make(view, "削除したい行事の入力をしてください", Snackbar.LENGTH_SHORT)
                        .setActionTextColor(Color.YELLOW)
                        .show()
            }else{
                for ( i in 1 .. values.size-1){
                    if(values[i].equals(binding.spinnertext2.text.toString())){
                    }else{
                        value2+=values[i].toString()
                    }
                }
                binding.spinnertext2.setText("")
                values=value2
                saveArray(values,"StringItem")
                Snackbar.make(view, "指定の項目の削除完了しました", Snackbar.LENGTH_SHORT)
                        .setActionTextColor(Color.YELLOW)
                        .show()
            }

        }
    }



}




