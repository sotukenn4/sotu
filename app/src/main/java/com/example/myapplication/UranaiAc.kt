package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
////////////////////////////////////////////いらないいらないボツ//////ここは使わない！！！！！
class UranaiAc : AppCompatActivity() {
    //日付を取得
    val date= getCurrentDateTime()
    val dateInString= date.toString("yyyy/MM/dd")

    //ファイルを作成
    private var file: File? = null
    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter= SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView : TextView = findViewById(R.id.textView)
        val textView2 : TextView = findViewById(R.id.textView2)
        //textView.text = dateInString

        val button : Button = findViewById(R.id.button)

        val fileName = "uranai.txt"
        file = File(applicationContext.filesDir, fileName)
        //val check: String? = readFile()

        val str = readFiles(fileName)
        //日付がテキストファイルに書かれているかどうか
        if (str != null) {
            textView2.text = "今日の運勢は?"
        } else {
            textView.text = "ああああ"
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
                //ボタンを非表示にする
                button.setVisibility(View.INVISIBLE)
            }else{
                textView.text = " "
                textView2.text = "今日は終わり!!また明日"
                //ボタンを非表示にする
                button.setVisibility(View.INVISIBLE)
            }
        }
        //strの中身の日付が違うならボタンが表示される
        /*if(str!=dateInString){
        button.setVisibility(View.VISIBLE)
    }*/


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


}