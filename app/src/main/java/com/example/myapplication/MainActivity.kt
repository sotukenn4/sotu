package com.example.myapplication

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.*
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
    val fileName = "haikei.txt"
    private var files: File? = null
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
        //+ボタン押されたときに予定編集画面に移動する　　途中エラーでたで大変だった
        binding.fab.setOnClickListener{ view->
        naviController.navigate(R.id.action_to_scheduleEditFragment)
        }
        //supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLUE))
        //tenkiがFragmentにできなかったときは右上にボタンを設定して遷移させる＊＊（理由：下のツールバーから選択時
    // にtenkiだけが画面変わるから違和感ある）
        /*binding.weather.setOnClickListener {
            val intent = Intent(applicationContext, tenki::class.java)
            startActivity(intent)
        }*/// getSharedPreferencesメソッドでSharedPreferencesオブジェクトを取得
        // getString()を呼び出して保存されている文字列を読み込む
    }//onCreate終わり
    fun setFabVisible(visibility: Int){
        binding.fab.visibility = visibility
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

    // ファイルを保存
    fun saveFile(str: String?) {
        // try-with-resources
        try { FileWriter(files).use { writer -> writer.write(str) } } catch (e: IOException) { e.printStackTrace() }
    }

    // ファイルを読み出し
    fun readFile(fileName: String): String? {
        var text: String? = null
        // try-with-resources
        try { BufferedReader(FileReader(files)).use { br -> text = br.readLine() } } catch (e: IOException) { e.printStackTrace() }
        return text
    }
    //これなんだったけ？わすれたで暇なときに見るわ　///////これ調べたら、戻るボタンが押されたときに一つまえに戻るようにしてるらしいｙ　11/26
    override fun onSupportNavigateUp()=findNavController(R.id.nav_host_fragment).navigateUp()

    //背景画面変更メソッド　この処理考えた俺すげーって思った。
    fun HaikeiChange(imageBack: String?) {
        if (imageBack != null||imageBack!="null") {
            if (imageBack != null) {
                binding.backimage.setImageURI(imageBack.toUri())
            }
        }
    }

    //透明度変更メソッド　これかくの大変だったわ。もう少し楽なやり方あっただろ！！くぅーん
    fun ToumeidoChange(toumei: Int?){
        if (toumei!= null) {
            //透明度０
            if(toumei==0) {
                binding.backimage.setAlpha(0)
                //透明度１
            }else  if(toumei==1) {binding.backimage.setAlpha(25)
                //透明度２
            }else  if(toumei==2) {binding.backimage.setAlpha(52)
                //透明度３
            }else  if(toumei==3) {binding.backimage.setAlpha(77)
                //透明度４
            }else  if(toumei==4) {binding.backimage.setAlpha(102)
                //透明度５
            }else  if(toumei==5) {binding.backimage.setAlpha(127)
                //透明度６
            }else  if(toumei==6) {binding.backimage.setAlpha(152)
                //透明度７
            }else  if(toumei==7) {binding.backimage.setAlpha(177)
                //透明度８
            }else  if(toumei==8) {binding.backimage.setAlpha(202)
                //透明度９
            }else  if(toumei==9) {binding.backimage.setAlpha(227)
                //透明度１０
            }else  if(toumei==10) {binding.backimage.setAlpha(255)
            }
        }
    }


}




