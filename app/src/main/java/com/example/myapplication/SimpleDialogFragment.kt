package com.example.myapplication

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController

class SimpleDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("変更しますか？")
            .setMessage("")
            .setPositiveButton("更新") { dialog, id ->
                id.let {
                    val action=
                        FirstFragmentDirections.actionToScheduleEditFragment(it.toLong())
                    findNavController().navigate(action)
                }

            }
            .setNegativeButton("削除") { dialog, id ->
                println("dialog:$dialog which:$id")
            }

        return builder.create()
    }

}