package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.provider.ContactsContract.Intents.Insert.DATA
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_sample.*
import kotlinx.android.synthetic.main.activity_tenki.*
import java.util.*


class SampleActivity : AppCompatActivity() {
    private val REQUEST_GALLERY_TAKE = 2
    private val RECORD_REQUEST_CODE = 1000
    private lateinit var storage_iv: ImageView
    private lateinit var storage_btn: Button
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        val intent = intent
        storage_iv = findViewById<ImageView>(R.id.storage_iv)
        storage_btn = findViewById<Button>(R.id.storage_btn)
        setupPermissions()
        //EditTextのクリックイベントを設定
        storage_btn.setOnClickListener {
            openGalleryForImage()
        }
    }

    //ギャラリーを開くためのメソッド
    private fun openGalleryForImage() {
        //ギャラリーに画面を遷移するためのIntent
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY_TAKE)
    }


    // onActivityResultにイメージ設定
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode){
            2 -> {
                if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY_TAKE){
                    //選択された写真にImageViewを変更
                    storage_iv.setImageURI(data?.data) // handle chosen image
                    val intent = Intent()
                    intent.putExtra("E", (data?.data).toString());
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    //パーミッションのチェックを設定するためのメソッド
    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    //パーミッションをリクエストするためのメソッド
    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            RECORD_REQUEST_CODE)
    }

    //パーミッションの許可の結果による実行されるメソッド
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when(requestCode){
            RECORD_REQUEST_CODE ->{
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(applicationContext, "デバイス内の写真やメディアへのアクセスが許可されませんでした。", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(applicationContext, "デバイス内の写真やメディアへのアクセスが許可されました。", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

}


