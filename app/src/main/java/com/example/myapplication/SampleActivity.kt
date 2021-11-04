package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_sample.*


class SampleActivity : AppCompatActivity() {
    private val REQUEST_GALLERY_TAKE = 2
    private val RECORD_REQUEST_CODE = 1000
    private lateinit var storage_iv: ImageView
    private lateinit var storage_btn: Button
    private var prog=5
    private var Syasin: String? =null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        storage_iv = findViewById(R.id.storage_iv)
        storage_btn = findViewById(R.id.storage_btn)
        setupPermissions()

        //EditTextのクリックイベントを設定
        storage_btn.setOnClickListener {
            openGalleryForImage()
        }
        button2.setOnClickListener {
            val intent = Intent()
            intent.putExtra("E", Syasin)
            intent.putExtra("progress", prog)
            setResult(RESULT_OK, intent)
            finish()
        }

        seekBar2.progress = 5
        seekBar2.max = 10
        //seekBar2.min = 0

        seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            // 値が変更された時に呼ばれる
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                prog=progress
            }

            // つまみがタッチされた時に呼ばれる
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            // つまみが離された時に呼ばれる
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    //ギャラリーを開くためのメソッド
    private fun openGalleryForImage() {
        //ギャラリーに画面を遷移するためのIntent
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY_TAKE)
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
    // onActivityResultにイメージ設定
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode){
            2 -> {
                if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY_TAKE){
                    //選択された写真にImageViewを変更
                    storage_iv.setImageURI(data?.data) // handle chosen image
                    Syasin=(data?.data).toString()

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


