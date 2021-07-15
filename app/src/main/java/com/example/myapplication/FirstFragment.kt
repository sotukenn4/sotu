package com.example.myapplication

import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
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
    private  val args: FirstFragmentArgs by navArgs()
    val strList = arrayOf("更新","削除","キャンセル")
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
        adapter.setOnLongClickListener {
                id ->
            id?.let {
                val dialog = ConfirmDialog(
                    "変更しますか？",
                    "削除", { deleteSchedule(view,it)
                        adapter.notifyDataSetChanged()},
                    "更新", {
                        val action=
                            FirstFragmentDirections.actionToScheduleEditFragment(it)
                        findNavController().navigate(action)
                    }
                )
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
    private fun deleteSchedule(view: View,it:Long){

        realm.executeTransaction{
        db:Realm->db.where<Schedule>().equalTo("id",it)
            ?.findFirst()
            ?.deleteFromRealm()
        }
        Snackbar.make(view, "削除しました", Snackbar.LENGTH_SHORT)
            .setActionTextColor(Color.YELLOW)
            .show()


    }

}



