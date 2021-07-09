package com.example.myapplication

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import java.lang.IllegalStateException


class ListDialogFragment: DialogFragment() {
    val strList = arrayOf("更新","削除","キャンセル")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog=activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("変更しますか？")
            builder.setPositiveButton("変更",DialogButtonClickListener())
            builder.setNegativeButton("削除",DialogButtonClickListener())
            builder.setNeutralButton("キャンセル",DialogButtonClickListener())
            builder.create()
        }
        return dialog?:throw IllegalStateException("アクティビティがnull")
    }
private inner class DialogButtonClickListener:DialogInterface.OnClickListener{
    override fun onClick(dialog: DialogInterface, which: Int) {
        var msg=""
        when(which) {
            DialogInterface.BUTTON_POSITIVE ->
                msg = "変更"

            DialogInterface.BUTTON_NEGATIVE ->
                msg="削除"
            DialogInterface.BUTTON_NEUTRAL ->
                msg = "キャンセル"
        }
        Toast.makeText(activity,msg,Toast.LENGTH_LONG).show()
    }

}
}





