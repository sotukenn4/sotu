package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat
import androidx.fragment.app.Fragment
import android.widget.*
import kotlinx.android.synthetic.main.activity_tenki.*
import kotlinx.android.synthetic.main.activity_tenki.tvWeatherTelop
import kotlinx.android.synthetic.main.fragment_tamesi.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.util.concurrent.Executors

//////天気くらす
class tenki : AppCompatActivity() {

    //特定メソッドから他メソッドへの上書きグローバル変数
    var pointname = ""
    var weatherchan =""
    var change = ""
    var rainpercent = ""
    var mornchan = ""
    var noochan = ""
    var evechan = ""
    var nigchan = ""

    private var _list: MutableList<MutableMap<String, String>> =mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tenki)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        _list=createList()
        val lvCityList=findViewById<ListView>(R.id.lvCityList)
        val from= arrayOf("name")
        val to = intArrayOf(android.R.id.text1)
        val adapter= SimpleAdapter(this, _list, android.R.layout.simple_list_item_1, from, to)
        lvCityList.adapter=adapter
        lvCityList.onItemClickListener=ListItemClickListener()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            return true
        }
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    private fun createList(): MutableList<MutableMap<String, String >> {
        val list:MutableList<MutableMap<String, String >> = mutableListOf()
        var city= mutableMapOf("name" to "愛知", "q" to "Aichi" )
        list.add(city)
        city= mutableMapOf("name" to "三重", "q" to "Mie")
        list.add(city)
        city= mutableMapOf("name" to "岐阜", "q" to "Gifu")
        list.add(city)
        city= mutableMapOf("name" to "名古屋", "q" to "Nagoya")
        list.add(city)
        city= mutableMapOf("name" to "一宮", "q" to "Ichinomiya")
        list.add(city)
        return list
    }

    //グローバル変数への場所値の代入
    private fun point(data: String?) {
        if (data != null) {
            pointname= data
        }
    }
    private fun whe(data: String?) {
        if (data != null) {
            if (data == "愛知") {
                weatherchan = "晴れ"
                change = "19"
                rainpercent = "20"
                mornchan = "11"
                noochan = "23"
                evechan = "16"
                nigchan = "20"
            }
            else{
                weatherchan = "くもり"
                change = "10"
                rainpercent = "10"
                mornchan = "13"
                noochan = "21"
                evechan = "16"
                nigchan = "10"
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private inner class ListItemClickListener: AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            textList.visibility = View.INVISIBLE
            textinv.visibility = View.INVISIBLE

            val item=_list.get(position)
            //val q =item.get("q")
            val name = item.get("name")
            point(name)
            whe(name)



            val telop="今日の ${pointname} の天気"
            val tvWeatherTelop=findViewById<TextView>(R.id.tvWeatherTelop)
            tvWeatherTelop.text = telop


            //天気

            val desc="現在は ${weatherchan} です。"
            val tvWeatherDesc=findViewById<TextView>(R.id.tvWeatherDesc)
            tvWeatherDesc.text = desc

            val deg = "気温  ${change} 度"
            val tvWeatherDegree=findViewById<TextView>(R.id.tvWeatherdegree)
            tvWeatherDegree.text = deg

            //降水確率

            val dayrain = "${rainpercent}%"
            val tvWeatherRain = findViewById<TextView>(R.id.tvrain)
            tvWeatherRain.text = dayrain


            val daymorn = " ${mornchan} 度"
            val tvWeatherMorn=findViewById<TextView>(R.id.morningtxt)
            tvWeatherMorn.text = daymorn
            val daynoon = " ${noochan} 度"
            val tvWeatherNoo=findViewById<TextView>(R.id.afternoontxt)
            tvWeatherNoo.text = daynoon
            val dayeve = " ${evechan} 度"
            val tvWeatherEve=findViewById<TextView>(R.id.eveningtxt)
            tvWeatherEve.text = dayeve
            val daynight = " ${nigchan} 度"
            val tvWeatherNight=findViewById<TextView>(R.id.nighttxt)
            tvWeatherNight.text = daynight

        }
    }
}