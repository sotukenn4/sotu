package com.example.myapplication

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment


class MyDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val choices = arrayOf("A", "B", "C")
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("タイトル")
            .setPositiveButton("OK") { dialog, id ->
                // このボタンを押した時の処理を書きます。
            }
            .setItems(choices) { dialog, which ->
                Toast.makeText(
                    activity, String.format("「%s」を選択しました。", choices[which]),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        return builder.create()
    }
}