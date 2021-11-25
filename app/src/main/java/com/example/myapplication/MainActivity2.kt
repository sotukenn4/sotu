package com.example.myapplication

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity2 : AppCompatActivity() {
    //日付を取得
    val dates= getCurrentDateTime()
    val dateInString= dates.toString("yyyy/MM/dd")

    //ファイルを作成 日付
    private var files: File? = null
    //ファイルを作成 運
    private var fileun: File? = null
    //ファイルを作成 運2
    private var fileun2: File? = null
    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter= SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.MAGENTA))
        val textView : TextView = findViewById(R.id.textView)
        val textView2 : TextView = findViewById(R.id.textView2)
        //textView.text = dateInString

        val button : Button = findViewById(R.id.button)

        val fileName = "uranai.txt"
        files = File(applicationContext.filesDir, fileName)
        //val check: String? = readFile()
        val str = readFiles(fileName)

        val fileName2 = "uranaikekka.txt"
        fileun = File(applicationContext.filesDir, fileName2)
        //val check: String? = readFile()
        val kekka = readFiles(fileName2)

        val fileName3 = "uranaikekka2.txt"
        fileun = File(applicationContext.filesDir, fileName3)
        //val check: String? = readFile()
        val kekka2 = readFiles(fileName3)
        //日付がテキストファイルに書かれているかどうか
        if (str != null) {
            if(str != dateInString){
                textView2.text = "今日の運勢は?"

            }else {
                textView.text = kekka.toString()
                textView2.text=kekka2.toString()
                textView6.text = "今日は終わり!!また明日"
                //ボタンを非表示にする
                button.setVisibility(View.INVISIBLE)
            }
        } else {
            textView.text = "今日の運勢は?"
        }
        button.setOnClickListener {
            //strの中身が違うならtrue
            if (str != dateInString) {
                //占い結果をランダムで出現させる
                val resResult: Fortune = Fortune().getFortune()
                textView.text = resResult.name
                textView2.text = resResult.description
                File(applicationContext.filesDir, fileName).writer().use {
                    it.write(dateInString)
                }
                File(applicationContext.filesDir, fileName2).writer().use {
                    it.write(resResult.name)
                }
                File(applicationContext.filesDir, fileName3).writer().use {
                    it.write(resResult.description)
                }
                //ボタンを非表示にする
                button.setVisibility(View.INVISIBLE)
            }else{
                /*textView.text = " "
                textView2.text = "今日は終わり!!また明日"
                //ボタンを非表示にする
                button.setVisibility(View.INVISIBLE)*/
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.itembar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete ->
                finish()
        }
        return true
    }
    private fun readFiles(file: String): String? {

        // to check whether file exists or not
        val readFile = File(applicationContext.filesDir, file)

        if(!readFile.exists()){
            Log.d("debug","No file exists")
            return null
        }
        else{
            return readFile.bufferedReader().use(BufferedReader::readText)
        }
    }
    // ファイルを保存
    fun saveFile(str: String?) {
        // try-with-resources
        try {
            FileWriter(files).use { writer -> writer.write(str) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // ファイルを読み出し
    fun readFile(): String? {
        var text: String? = null
        // try-with-resources
        try {
            BufferedReader(FileReader(files)).use { br -> text = br.readLine() }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return text
    }


}