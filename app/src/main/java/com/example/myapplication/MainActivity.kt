package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*

//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.fragment_first.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: ConstraintLayout
    private var inputMethodManager: InputMethodManager? = null
    private var _list: MutableList<MutableMap<String,String>> =mutableListOf()
    //private  lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))
        //val prefs2: SharedPreferences =  this.getSharedPreferences("DATA", Context.MODE_PRIVATE)
        //ナビゲーションを使えるようにする
        val naviController=findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(naviController)
        val bottomNavView: BottomNavigationView = findViewById(R.id.bottom_nav)
        NavigationUI.setupWithNavController(bottomNavView, naviController)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLUE))

    }

//天気Activityから戻った時に一覧画面に戻る
    /*override fun onRestart() {
        super.onRestart()
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        val res = intent?.getStringExtra("E")
        val progress1= (intent?.getIntExtra("progress",10))
        if (res != null) {
            binding.imageView3.setImageURI(res.toUri())
        }

    if (progress1!= null) {
        if(progress1==0) {
            binding.imageView3.setAlpha(0)
        }else  if(progress1==1) {binding.imageView3.setAlpha(25)
        }else  if(progress1==2) {binding.imageView3.setAlpha(52)
        }else  if(progress1==3) {binding.imageView3.setAlpha(77)
        }else  if(progress1==4) {binding.imageView3.setAlpha(102)
        }else  if(progress1==5) {binding.imageView3.setAlpha(127)
        }else  if(progress1==6) {binding.imageView3.setAlpha(152)
        }else  if(progress1==7) {binding.imageView3.setAlpha(177)
        }else  if(progress1==8) {binding.imageView3.setAlpha(202)
        }else  if(progress1==9) {binding.imageView3.setAlpha(227)
        }else  if(progress1==10) {binding.imageView3.setAlpha(255)
        }
    }
    }
    override fun onSupportNavigateUp()=findNavController(R.id.nav_host_fragment).navigateUp()


}


