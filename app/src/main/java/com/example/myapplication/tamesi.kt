package com.example.myapplication

//import kotlinx.android.synthetic.main.fragment_tamesi.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SimpleAdapter
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.core.os.HandlerCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentTamesiBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.util.concurrent.Executors


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [tamesi.newInstance] factory method to
 * create an instance of this fragment.
 */

class tamesi : Fragment() {
    private var _binding:FragmentTamesiBinding?=null
    private  val binding get()=_binding!!
    companion object{
        private const val DEBUG_TAG="AsyncSample"
        private const val WEATHERINFO_URL="https://api.openweathermap.org/data/2.5/weather?lang=ja"
        private const val APP_ID="83311ce95b6808b861ddd5e178b11b87"
    }
    private var _list: MutableList<MutableMap<String, String>> =mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _list=createList()
        val from= arrayOf("name")
        val to = intArrayOf(android.R.id.text1)
        val adapter= SimpleAdapter(this.context, _list, android.R.layout.simple_list_item_1, from, to)
        binding.lvCityList.adapter=adapter
        binding.lvCityList.onItemClickListener=ListItemClickListener()
        val looper = Looper.getMainLooper()
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentTamesiBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun createList(): MutableList<MutableMap<String, String>> {
        var list:MutableList<MutableMap<String, String>> = mutableListOf()
        var city= mutableMapOf("name" to "愛知", "q" to "Aichi")
        list.add(city)
        city= mutableMapOf("name" to "大阪", "q" to "Osaka")
        list.add(city)
        city= mutableMapOf("name" to "岐阜", "q" to "Gifu")
        list.add(city)
        return list


    }
    private fun receiveWeatherInfo(urlFull: String){

        val handler= HandlerCompat.createAsync(Looper.getMainLooper())
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
                  //  Log.w(tenki.DEBUG_TAG, "通信タイムアウト", ex)
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
            val telop="${cityName}の天気"
            val desc="現在は${weather}です。\n緯度は${latitude}度で軽度は${longitude}です。"
            binding.tvWeatherTelop.text=telop
            binding.tvWeatherDesc.text=desc
        }
    }



    private inner class ListItemClickListener: AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val item=_list.get(position)
            val q =item.get("q")
            q?.let {
                val urlFull="${WEATHERINFO_URL}&q=$q&appid=${APP_ID}"
                receiveWeatherInfo(urlFull)
            }
        }
    }



}
///////////////このクラスは使わない