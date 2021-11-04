package com.example.myapplication

import android.app.ActionBar
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentTextColorChangeBinding
import com.google.android.material.snackbar.Snackbar
import java.io.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TextColorChange.newInstance] factory method to
 * create an instance of this fragment.
 */
class TextColorChange : Fragment() {
    private var _binding: FragmentTextColorChangeBinding?=null
    private  val binding get()=_binding!!
    private var file: File? = null
private lateinit var color :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private inner class ListItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if(position==0){
                color="#000000"
                saveFile(color)
            }else if(position==1){
                color="#FFFFFF"
                saveFile(color)
            }else if(position==2){
                color="#FF0000"
                saveFile(color)
            }else if(position==3){
                color="#0000FF"
                saveFile(color)
            }else if(position==4){
                color="#008000"
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listcolor.onItemClickListener = ListItemClickListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentTextColorChangeBinding.inflate(inflater, container, false)
        val fileName = "TestFile.txt"
        file = File(requireContext().filesDir, fileName)
        val str: String? = readFile()
        binding.textView10.setTextColor(Color.parseColor(str))
        return binding.root
    }


}