package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.fragment_first.*


class MainActivity<T> : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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

        binding.fab.setOnClickListener { view ->
            naviController.navigate(R.id.action_to_scheduleEditFragment)
        }


        //tenkiがFragmentにできなかったときは右上にボタンを設定して遷移させる＊＊（理由：下のツールバーから選択時
    // にtenkiだけが画面変わるから違和感ある）
        /*binding.weather.setOnClickListener {
            val intent = Intent(applicationContext, tenki::class.java)
            startActivity(intent)
        }*/// getSharedPreferencesメソッドでSharedPreferencesオブジェクトを取得


        // getString()を呼び出して保存されている文字列を読み込む


    }

    //天気Activityから戻った時に一覧画面に戻る＊＊これをしないとtenki画面から戻ったときに下のツールバーがバグる。
    //栗本君がFragment成功できればいらない。
    //しかし、これを実行させると背景画面から戻り時に指定した背景が反映されない。対処方は指定した背景画像保存して画面
    //作成時に読み込む。だが、できない。課題①
    /*override fun onRestart() {
        super.onRestart()
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }*/


//Activityを離れて戻ってきた時に最初に行う処理
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
    //ImageGroundに背景変更画面で保存した画像データを格納。何も格納されていなければnullを格納。データ型：String型
        val ImageGround = intent?.getStringExtra("E")
    //Toumeidoに背景変更画面で保存した透明度を格納。とくに指定がなければMAX10にする。データ型：Int型
        val Toumeido= (intent?.getIntExtra("progress",10))
    //透明度変更処理
   ToumeidoChange(Toumeido)
    //背景画像変更処理。引数:ImageGround
        HaikeiChange(ImageGround)
    }

    override fun onSupportNavigateUp()=findNavController(R.id.nav_host_fragment).navigateUp()
    fun setFabVisible(visibility: Int){
        binding.fab.visibility=visibility
    }

    //背景画面変更メソッド
    fun HaikeiChange(imageBack: String?) {
        if (imageBack != null||imageBack!="null") {
            if (imageBack != null) {
                binding.imageView3.setImageURI(imageBack.toUri())
            }
        }
    }
    //透明度変更メソッド
    fun ToumeidoChange(toumei: Int?){
        if (toumei!= null) {
            //透明度０
            if(toumei==0) {
                binding.imageView3.setAlpha(0)
                //透明度１
            }else  if(toumei==1) {binding.imageView3.setAlpha(25)
                //透明度２
            }else  if(toumei==2) {binding.imageView3.setAlpha(52)
                //透明度３
            }else  if(toumei==3) {binding.imageView3.setAlpha(77)
                //透明度４
            }else  if(toumei==4) {binding.imageView3.setAlpha(102)
                //透明度５
            }else  if(toumei==5) {binding.imageView3.setAlpha(127)
                //透明度６
            }else  if(toumei==6) {binding.imageView3.setAlpha(152)
                //透明度７
            }else  if(toumei==7) {binding.imageView3.setAlpha(177)
                //透明度８
            }else  if(toumei==8) {binding.imageView3.setAlpha(202)
                //透明度９
            }else  if(toumei==9) {binding.imageView3.setAlpha(227)
                //透明度１０
            }else  if(toumei==10) {binding.imageView3.setAlpha(255)
            }
        }
    }




}




