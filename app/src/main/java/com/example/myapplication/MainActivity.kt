package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
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
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.fragment_first.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ConstraintLayout
    private var inputMethodManager: InputMethodManager? = null
    private var _list: MutableList<MutableMap<String,String>> =mutableListOf()
    //private  lateinit var realm: Realm
    //保存データを取り出し、valuesに格納
    var values = arrayOf(" ")
    //予備の配列。0番目に空白
    var value2 = arrayOf(" ")
    var timer: Timer? = null
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
        //tenkiがFragmentにできなかったときは右上にボタンを設定して遷移させる＊＊（理由：下のツールバーから選択時
    // にtenkiだけが画面変わるから違和感ある）
        /*binding.weather.setOnClickListener {
            val intent = Intent(applicationContext, tenki::class.java)
            startActivity(intent)
        }*/

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
    //ImageGroundに背景変更画面で保存した画像データを格納。データ型：String型
        val ImageGround = intent?.getStringExtra("E")
    //Toumeidoに背景変更画面で保存した透明度を格納。データ型：Int型
        val Toumeido= (intent?.getIntExtra("progress",10))
    //透明度変更処理
    if (Toumeido!= null) {
        if(Toumeido==0) {
            binding.imageView3.setAlpha(0)
        }else  if(Toumeido==1) {binding.imageView3.setAlpha(25)
        }else  if(Toumeido==2) {binding.imageView3.setAlpha(52)
        }else  if(Toumeido==3) {binding.imageView3.setAlpha(77)
        }else  if(Toumeido==4) {binding.imageView3.setAlpha(102)
        }else  if(Toumeido==5) {binding.imageView3.setAlpha(127)
        }else  if(Toumeido==6) {binding.imageView3.setAlpha(152)
        }else  if(Toumeido==7) {binding.imageView3.setAlpha(177)
        }else  if(Toumeido==8) {binding.imageView3.setAlpha(202)
        }else  if(Toumeido==9) {binding.imageView3.setAlpha(227)
        }else  if(Toumeido==10) {binding.imageView3.setAlpha(255)
        }
    }
    //背景画像変更処理。引数:ImageGround
        HaikeiChange(ImageGround)
    }
    //これなんだったけ？わすれたで暇なときに見るわ
    override fun onSupportNavigateUp()=findNavController(R.id.nav_host_fragment).navigateUp()
    //背景画面変更メソッド
    fun HaikeiChange(imageBack: String?) {
        if (imageBack != null||imageBack!="null") {
            if (imageBack != null) {
                binding.imageView3.setImageURI(imageBack.toUri())
            }
        }
    }

}




