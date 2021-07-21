package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySampleBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_sample.*


class SampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySampleBinding
    val list = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");
        list.add("G");
        list.add("H");
        list.add("I");
        list.add("J");
        list.add("K");
        list.add("L");
        val mThumbIds = arrayOf(
            R.drawable.carender, R.drawable.itiran,
            R.drawable.option, R.drawable.tenki
        )
        gridView1.setAdapter(ArrayAdapter(this, android.R.layout.simple_list_item_1, mThumbIds))
        gridView1.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            val message: String = list.get(position).toString() + "が選択されました。"
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.YELLOW)
                .show()
        })
    }
}