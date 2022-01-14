package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat
import kotlinx.android.synthetic.main.activity_tenki.*
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
    var la=""
    var lo=""

    //URL情報等の変数化
    companion object{
        private const val DEBUG_TAG="AsyncSample"
        //URLのhttpsをhttpに直したら実機でできるようになったじゃねーか。ばかやろう、うれしすぎて涙止まらねぇ。できてよかった（笑）
        private const val WEATHERDAY_URL="http://api.openweathermap.org/data/2.5/onecall?lang=ja&units=metric"
        //private const val WEATHERDAY_URL="https://api.openweathermap.org/data/2.5/weather?lang=ja"
        private const val APP_ID="83311ce95b6808b861ddd5e178b11b87"
    }

    private var _list: MutableList<MutableMap<String, String>> =mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_tenki)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.rgb(255,165,0)))


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

    //追加
   private fun receiveWeatherDAY(urlDay: String){
        val handler= HandlerCompat.createAsync(mainLooper)
        val backgroundReceiver=WeatherInfoBackgroundReceiver(handler, urlDay)
        val executeService= Executors.newSingleThreadExecutor()
        executeService.submit(backgroundReceiver)
    }

    /*
    private fun receiveWeatherInfo(urlFull: String){
        val handler= HandlerCompat.createAsync(mainLooper)
        val backgroundReceiver=WeatherInfoBackgroundReceiver(handler, urlFull)
        val executeService= Executors.newSingleThreadExecutor()
        executeService.submit(backgroundReceiver)
    }
    */

    //非同期とurl解析
    private inner class WeatherInfoBackgroundReceiver(handler: Handler, url: String): Runnable {
        private val  _handler=handler
        private  val _url=url
        @WorkerThread
        override fun run() {
            var result =""
            val url= URL(_url)
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
    //メイン
    private inner class WeatherInfoPostExecutor(result: String):Runnable{
        private  val _result=result
        @UiThread
        override fun run() {

            //ワンコールAPIでのJSONデータ解析
            val rootsubJSON= JSONObject(_result)

            //グローバル変数からの呼び出し
            val telop="今日の ${pointname} の天気"
            val tvWeatherTelop=findViewById<TextView>(R.id.tvWeatherTelop)
            tvWeatherTelop.text = telop


            //天気
            val weatherJSONArray=rootsubJSON.getJSONObject("current")
            val weatherJSON=weatherJSONArray.getJSONArray("weather")
            val weatherJSONsub=weatherJSON.getJSONObject(0)
            val weather=weatherJSONsub.getString("description")
            val weatherchan = weatherchange(weather)
            val desc="現在は ${weatherchan} です。"
            val tvWeatherDesc=findViewById<TextView>(R.id.tvWeatherDesc)
            tvWeatherDesc.text = desc

            //気温
            //val degreeJSONArray=rootsubJSON.getJSONObject("current")
            val degree=weatherJSONArray.getString("temp")
            val change = chandeg(degree)
            val deg = "気温  ${change}°c"
            val tvWeatherDegree=findViewById<TextView>(R.id.tvWeatherdegree)
            tvWeatherDegree.text = deg


            //降水確率
            val dayJSONArrayrain=rootsubJSON.getJSONArray("daily")
            val dayJSONrain=dayJSONArrayrain.getJSONObject(0)
            val rain=dayJSONrain.getString("pop")
            val rainpercent= rainchange(rain)
            val dayrain = " ${rainpercent} %"
            val tvWeatherRain = findViewById<TextView>(R.id.tvrain)
            tvWeatherRain.text = dayrain

            //一日の気温
            //val dayJSONArray=rootsubJSON.getJSONArray("daily")
            //val dayJSON=dayJSONArray.getJSONObject(0)
            val dayJSONsub=dayJSONrain.getJSONObject("temp")

            val morn=dayJSONsub.getString("morn")
            val noo=dayJSONsub.getString("day")
            val eve=dayJSONsub.getString("eve")
            val nig=dayJSONsub.getString("night")
            val mornchan = chandeg(morn)
            val noochan = chandeg(noo)
            val evechan = chandeg(eve)
            val nigchan = chandeg(nig)

            val daymorn = "朝 ${mornchan}°c"
            val tvWeatherMorn=findViewById<TextView>(R.id.morningtxt)
            tvWeatherMorn.text = daymorn
            val daynoon = "昼 ${noochan}°c"
            val tvWeatherNoo=findViewById<TextView>(R.id.afternoontxt)
            tvWeatherNoo.text = daynoon
            val dayeve = "夕 ${evechan}°c"
            val tvWeatherEve=findViewById<TextView>(R.id.eveningtxt)
            tvWeatherEve.text = dayeve
            val daynight = "夜 ${nigchan}°c"
            val tvWeatherNight=findViewById<TextView>(R.id.nighttxt)
            tvWeatherNight.text = daynight

        }
    }

    //気温round
    private fun chandeg(degree: String ): String  {
        val x = degree
        val y = Math.round((x.toDouble()) * 10.0) / 10.0
        return y.toString()
    }
    //降水確率
    private fun rainchange(rain: String): String {
        val x = rain
        val y = Math.round((x.toDouble()) * 100)
        return  y.toString()
    }
    //グローバル変数への場所値の代入
    private fun point(data: String?) {
        if (data != null) {
            pointname= data
        }
    }
    
    //付け焼刃用　翻訳
    private fun weatherchange(data: String): String {
        var String = ""
        if(data == "雷"){
            String = "かみなり"
        }
        else if (data == "雲") {
            String = "くもり"
        }
        else if (data == "曇りがち") {
            String = "くもり"
        }
        else if (data == "厚い雲") {
            String = "くもり"
        }
        else if (data == "薄い雲") {
            String = "くもり"
        }
        else if (data == "適度な雨") {
            String = "小雨"
        }
        else if (data == "晴天") {
            String = "晴れ"
        }
        else String = data
        return String
    }

    private  fun posiname(data: String?){
        if (data != null) {
            if(data == "愛知"){
                la="35.180"
                lo="136.907"
            }
            if(data == "三重"){
                la="34.521"
                lo="136.383"
            }
            if(data == "岐阜"){
                la="35.423"
                lo="136.760"
            }
            if(data == "名古屋"){
                la="35.181"
                lo="136.906"
            }
            if(data == "一宮") {
                la = "35.300"
                lo = "136.800"
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private inner class ListItemClickListener: AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            //textList.visibility = View.INVISIBLE
            textinv.visibility = View.INVISIBLE

            val item=_list.get(position)
            //val q =item.get("q")
            val name = item.get("name")
            posiname(name)
            point(name)
/*
            val tvWeatherTelop=findViewById<TextView>(R.id.tvWeatherTelop)
            val tvWeatherDesc=findViewById<TextView>(R.id.tvWeatherDesc)
            val tvWeatherDegree=findViewById<TextView>(R.id.tvWeatherdegree)
            val tvWeatherRain = findViewById<TextView>(R.id.tvrain)
            val tvWeatherMorn=findViewById<TextView>(R.id.morningtxt)
            val tvWeatherNoo=findViewById<TextView>(R.id.afternoontxt)
            val tvWeatherEve=findViewById<TextView>(R.id.eveningtxt)
            val tvWeatherNight=findViewById<TextView>(R.id.nighttxt)

            if(name=="愛知") {
                tvWeatherTelop.text = "今日の愛知の天気"
                tvWeatherDesc.text = "現在は雨です"
                tvWeatherDesc.setTextSize(11.0f)
                tvWeatherDegree.text = "気温11度"
                tvWeatherRain.text = "90%"
                tvWeatherMorn.text = "11度"
                tvWeatherNoo.text = "11度"
                tvWeatherEve.text = "9度"
                tvWeatherNight.text = "9度"
            }else if(name=="三重"){
                tvWeatherTelop.text = "今日の三重の天気"
                tvWeatherDesc.text = "現在は雨です"
                tvWeatherDegree.text = "気温18度"
                tvWeatherRain.text = "40%"
                tvWeatherMorn.text = "13度"
                tvWeatherNoo.text = "18度"
                tvWeatherEve.text = "14度"
                tvWeatherNight.text = "13度"
            }else{
                tvWeatherTelop.text = "今日の${name}の天気"
                tvWeatherDesc.text = "現在は晴れです"
                tvWeatherDegree.text = "気温12度"
                tvWeatherRain.text = "80%"
                tvWeatherMorn.text = "11度"
                tvWeatherNoo.text = "12度"
                tvWeatherEve.text = "11度"
                tvWeatherNight.text = "10度"
            }
            
*/
            val esc="exclude=minutely,hourly"

                name?.let {
                    val urlDay = "$WEATHERDAY_URL&lat=$la&lon=$lo&$esc&appid=$APP_ID"
                    receiveWeatherDAY(urlDay)

               }
        }
    }
}