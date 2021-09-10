package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.fragment_first.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: ConstraintLayout
    private var inputMethodManager: InputMethodManager? = null
    private var _list: MutableList<MutableMap<String,String>> =mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))
        //ナビゲーションを使えるようにする
        val naviController=findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(naviController)
        val bottomNavView: BottomNavigationView = findViewById(R.id.bottom_nav)
        NavigationUI.setupWithNavController(bottomNavView, naviController)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLUE))

    }
//天気Activityから戻った時に一覧画面に戻る
    override fun onRestart() {
        super.onRestart()
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onSupportNavigateUp()=findNavController(R.id.nav_host_fragment).navigateUp()


}


