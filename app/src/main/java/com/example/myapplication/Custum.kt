package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentCustumBinding


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }
    private fun getArray(PrefKey: String): Array<String> {
        val prefs2: SharedPreferences =  requireActivity().getSharedPreferences("Array", Context.MODE_PRIVATE)
        val stringItem = prefs2.getString(PrefKey, "d")
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
        binding.spinneradd.setOnClickListener{
            values+=binding.spinnertext.text.toString()
            binding.spinnertext.setText("")
            saveArray(values,"StringItem");
        }
        binding.spinnerdelete.setOnClickListener{

        }
    }

}




