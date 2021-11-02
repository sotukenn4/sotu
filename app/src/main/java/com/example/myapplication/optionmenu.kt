package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentOptionmenuBinding
import kotlinx.android.synthetic.main.fragment_addoption.*


class optionmenu : Fragment() {
    private var _binding: FragmentOptionmenuBinding?=null
    private  val binding get()=_binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    private inner class ListItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val item = parent?.getItemAtPosition(position) as String
           if(item.equals("一括処理行事追加　＞")){
               val action=
                   optionmenuDirections.actionOptionmenuToAddoptio()
               findNavController().navigate(action)
           }else if(item.equals("一括処理行事削除　＞")){
               val action=
                   optionmenuDirections.actionOptionmenuToDeleteOption()
               findNavController().navigate(action)
           }else if(item.equals("背景画像・透明度変更(別画面)　＞")){
               val intent = Intent(context, SampleActivity::class.java)
               val requestCode = 1000
               intent.putExtra(EXTRA_MESSAGE, "a");
               startActivityForResult(intent, requestCode)
           }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.onItemClickListener = ListItemClickListener()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding= FragmentOptionmenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            optionmenu().apply {

            }
    }
}