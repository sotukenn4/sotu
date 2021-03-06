package com.example.myapplication

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

//これわかりにくいけどMainActivity2こそが占い画面だからな。名前をこれにしたのは急いでたからはははははは
class MainActivity2 : AppCompatActivity() {
    //日付を取得
    val dates= getCurrentDateTime()
    var dateInString= dates.toString("yyyy/MM/dd")
    //ファイルを作成 日付
    private var files: File? = null
    //ファイルを作成 運
    private var fileun: File? = null
    //ファイルを作成 運2
    private var fileun2: File? = null
    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
//日付の書式だと思う。わすれた
    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter= SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val textView2: TextView = findViewById(R.id.textView2)
        val top: ImageView = findViewById(R.id.uranai_unsei)
        top.setImageResource(R.drawable.uranai_top)
        val color: TextView = findViewById(R.id.color)
        val item: TextView = findViewById(R.id.luckyitem)
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            val resResult: Fortune = Fortune().getFortune()
            val gazouArray = resources.obtainTypedArray(R.array.uranai_list)
            val rand = resResult.index
            val drawable : Drawable?= gazouArray.getDrawable(rand)
            top.setImageDrawable(drawable)
            textView2.text = resResult.description
            color.text = resResult.color
            item.text = resResult.lucky_item
        }
        /*
        val top: ImageView = findViewById(R.id.uranai_unsei)
        top.setImageResource(R.drawable.uranai_top)
        //上のバーの色をピンクにしてやったぜ
        supportActionBar?.setBackgroundDrawable(ColorDrawable(R.drawable.uranai))
        val textView : TextView = findViewById(R.id.textView)
        val textView2 : TextView = findViewById(R.id.textView2)
        val button : Button = findViewById(R.id.button)
        val image : ImageView = findViewById(R.id.imageView)
        image.setAlpha(102)
        //前回に占いしたときの日付が格納されている
        val fileName = "uranai.txt"
        files = File(applicationContext.filesDir, fileName)
        val str = readFiles(fileName)
        //前回の占い結果が格納されている　　大吉、凶など
        val fileName2 = "uranaikekka.txt"
        fileun = File(applicationContext.filesDir, fileName2)
        val kekka = readFiles(fileName2)
        //前回の占い結果の詳細が格納されている　今日は家からでないほうがいいんじゃない、、、、など
        val fileName3 = "uranaikekka2.txt"
        fileun = File(applicationContext.filesDir, fileName3)
        /*dateInString = "2021/12/4"
        File(applicationContext.filesDir, fileName).writer().use {
            it.write(dateInString)
        }*/
        val kekka2 = readFiles(fileName3)
        //日付がテキストファイルに書かれているかどうか
        if (str != null) {
            if(str != dateInString){
                //今日はまだ占いをしていないなら
                textView2.text = "今日の運勢は?"
            }else {
                //すでに今日は占いをしていたら。
                textView.text = kekka.toString()
                textView2.text=kekka2.toString()
                textView6.text = "今日は終わり!!また明日"
                //ボタンを非表示にする
                button.setVisibility(View.INVISIBLE)
            }
        } else {
            //新規端末の場合。なにもstrにはないので、if文にはいらないからここにいくように。書かないと多分アプリ落ちる
            textView.text = "今日の運勢は?"
        }
        //占うボタンが押された時の処理
        button.setOnClickListener {
            //strの中身が違うならtrue
            //if (str != dateInString) {
                //占い結果をランダムで出現させる
                val resResult: Fortune = Fortune().getFortune()
                val gazouArray = resources.obtainTypedArray(R.array.uranai_list)
                val rand = resResult.index
                val drawable : Drawable?= gazouArray.getDrawable(rand)
                top.setImageDrawable(drawable)
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
            //}else{ }
        }

         */
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.itembar,menu)
        return true
    }

    //×ボタンが押されたときに画面を閉じる
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