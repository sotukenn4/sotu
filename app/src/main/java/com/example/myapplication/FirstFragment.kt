package com.example.myapplication

import android.R
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentFirstBinding
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.where


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

class FirstFragment : Fragment() ,View.OnCreateContextMenuListener {
    private var _binding: FragmentFirstBinding?=null
    private  val binding get()=_binding!!
    private  lateinit var realm: Realm
    private  val args: ScheduleEditFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm= Realm.getDefaultInstance()




    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentFirstBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.list.layoutManager= LinearLayoutManager(context)
        val schedules=realm.where<Schedule>().findAll()
        val adapter=ScheduleAdapter(schedules)
        binding.list.adapter=adapter

        adapter.setOnItemClickListener { id->
            id?.let {
                val action=
                    FirstFragmentDirections.actionToScheduleEditFragment(it)
                findNavController().navigate(action)
            }
        }
        adapter.setOnLongClickListener { id->
            id?.let {
                val dialog=ConfirmDialog("削除しますか？",
                    "削除", {deleteSchedule(view)},
                    "キャンセル", {
                        Snackbar.make(view, "キャンセルしました", Snackbar.LENGTH_SHORT)
                            .show()
                    })
                dialog.show(parentFragmentManager, "delete_dialog")
            }
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
    private fun deleteSchedule(view: View){
        realm.executeTransaction{


        }
        Snackbar.make(view, "削除しました", Snackbar.LENGTH_SHORT)
            .setActionTextColor(Color.YELLOW)
            .show()
        findNavController()
    }
}



