package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySampleBinding
import com.example.myapplication.databinding.FragmentCustumBinding
import kotlinx.android.synthetic.main.activity_sample.*
import java.util.*


class SampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySampleBinding
    val mThumbIds = intArrayOf( R.drawable.hanabi,R.drawable.asuka)
    private val scenes = arrayOf(
        "花火",
    "アスカ"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding= ActivitySampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val emails = arrayOfNulls<String>(scenes.size)
        // nameからメルアド作成
        for (i in 0 until scenes.size) {
            emails[i] = java.lang.String.format(Locale.US, "%s@mail.co.jp", scenes.get(i))
        }
        // ListViewのインスタンスを生成
            val listview=findViewById<ListView>(R.id.listView)
        // BaseAdapter を継承したadapterのインスタンスを生成
        // レイアウトファイル list_items.xml を
        // activity_main.xml に inflate するためにadapterに引数として渡す

        // BaseAdapter を継承したadapterのインスタンスを生成
        // レイアウトファイル list_items.xml を
        // activity_main.xml に inflate するためにadapterに引数として渡す

        val adapter: BaseAdapter = ImageAdapter(
            this.applicationContext,
            R.layout.list_items, scenes, emails,mThumbIds
        )
        // ListViewにadapterをセット
        listview.setAdapter(adapter)
        // クリックリスナーをセット
        listview.onItemClickListener=ListItemClickListener()


    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    private inner class ListItemClickListener: AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val intent = Intent(
                applicationContext, FragmentCustumBinding::class.java
            )
            val selectedPhoto: Int = mThumbIds.get(position)
            // インテントにセット
            //intent.putExtra("Text", selectedText)
            //intent.putExtra("EXTRA_DATA", selectedPhoto)
            intent.putExtra("EXTRA_DATA", selectedPhoto)
            // SubActivityへ遷移
           //startActivity(intent)
            setResult(RESULT_OK, intent);
            finish()
        }
    }

}


