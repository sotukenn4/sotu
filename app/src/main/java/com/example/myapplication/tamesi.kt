package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.annotation.UiThread
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [tamesi.newInstance] factory method to
 * create an instance of this fragment.
 */
class tamesi : Fragment() {
    companion object{
        private const val DEBUG_TAG="AsyncSample"
        private const val WEATHERINFO_URL="https://api.openweathermap.org/data/2.5/weather?lang=ja"
        private const val APP_ID="83311ce95b6808b861ddd5e178b11b87"
    }
    private var _list: MutableList<MutableMap<String, String>> =mutableListOf()


}
///////////////このクラスは使わない