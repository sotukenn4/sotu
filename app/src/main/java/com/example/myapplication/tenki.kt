package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.util.concurrent.Executors
//////天気くらす
class tenki : AppCompatActivity() {//
    companion object{
        private const val DEBUG_TAG="AsyncSample"
        private const val WEATHERINFO_URL="https://api.openweathermap.org/data/2.5/weather?lang=ja&units=metric"
        private const val APP_ID="83311ce95b6808b861ddd5e178b11b87"

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
    private var _list: MutableList<MutableMap<String, String>> =mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tenki)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLUE))
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
    private fun createList(): MutableList<MutableMap<String, String>> {
        var list:MutableList<MutableMap<String, String>> = mutableListOf()
        var city= mutableMapOf("name" to "愛知", "q" to "Aichi")
        list.add(city)
        city= mutableMapOf("name" to "三重", "q" to "Mie")
        list.add(city)
        city= mutableMapOf("name" to "岐阜", "q" to "Gifu")
        list.add(city)
        city= mutableMapOf("name" to "名古屋", "q" to "Nagoya")
        list.add(city)
        return list
    }

    private fun receiveWeatherInfo(urlFull: String){
        val handler= HandlerCompat.createAsync(mainLooper)
        val backgroundReceiver=WeatherInfoBackgroundReceiver(handler, urlFull)
        val executeService= Executors.newSingleThreadExecutor()
        executeService.submit(backgroundReceiver)
    }
    private inner class WeatherInfoBackgroundReceiver(handler: Handler, url: String): Runnable {
        private val  _handler=handler
        private  val _url=url
        @WorkerThread
        override fun run() {
            var result=""
            var url= URL(_url)
            val con=url.openConnection() as? HttpURLConnection
            con?.let {
                try {
                    it.connectTimeout=1000
                    it.readTimeout=1000
                    it.requestMethod="GET"
                    it.connect()
                    val stream=it.inputStream
                    result=is2String(stream)
                    stream.close()
                }
                catch (ex: SocketTimeoutException){
                    Log.w(DEBUG_TAG, "通信タイムアウト", ex)
                    val tvWeatherTelop=findViewById<TextView>(R.id.tvWeatherTelop)
                    tvWeatherTelop.text = "タイム合おうと"
                }
                it.disconnect()
            }
            val postExecutor=WeatherInfoPostExecutor(result)
            _handler.post(postExecutor)
        }
        private fun is2String(stream: InputStream): String{
            val sb= StringBuilder()
            val reader= BufferedReader(InputStreamReader(stream, "UTF-8"))
            var line=reader.readLine()
            while (line!=null){
                sb.append(line)
                line=reader.readLine()
            }
            reader.close()
            return sb.toString()
        }
    }
    private inner class WeatherInfoPostExecutor(result: String):Runnable{
        private  val _result=result
        @UiThread
        override fun run() {
            val rootJSON= JSONObject(_result)
            val cityName=rootJSON.getString("name")
            val coordJSON=rootJSON.getJSONObject("coord")
            val latitude=coordJSON.getString("lat")
            val longitude=coordJSON.getString("lon")
            val weatherJSONArray=rootJSON.getJSONArray("weather")
            val weatherJSON=weatherJSONArray.getJSONObject(0)
            val weather=weatherJSON.getString("description")
            val degreeJSONArray=rootJSON.getJSONObject("main")
            val degree=degreeJSONArray.getString("temp")

            val change = chandeg(degree)
//
            val telop="現在の ${cityName} の天気"
            val desc="現在は ${weather} です。"
            val deg = "気温は ${change} 度です。"
            // \n緯度は${latitude}度で軽度は${longitude}です。"
            val tvWeatherTelop=findViewById<TextView>(R.id.tvWeatherTelop)
            val tvWeatherDesc=findViewById<TextView>(R.id.tvWeatherDesc)

            val tvWeatherDegree=findViewById<TextView>(R.id.tvWeatherdegree)
            tvWeatherTelop.text = telop
            tvWeatherDesc.text = desc
            tvWeatherDegree.text = deg
        }
    }

    //
    private fun chandeg(degree: String ): String  {
        val x = degree
        val y = Math.round((x.toDouble()) * 10.0) / 10.0
        return y.toString()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    private inner class ListItemClickListener: AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val item=_list.get(position)
            val q =item.get("q")//
            q?.let {
                val urlFull="${tenki.WEATHERINFO_URL}&q=$q&appid=${tenki.APP_ID}"
                receiveWeatherInfo(urlFull)
            }
        }
    }
}