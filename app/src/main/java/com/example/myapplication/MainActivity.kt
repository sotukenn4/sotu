package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.nfc.NfcAdapter.EXTRA_DATA
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.FragmentCustumBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var binding2: FragmentCustumBinding



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

    }
    override fun onSupportNavigateUp()=findNavController(R.id.nav_host_fragment).navigateUp()
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent);
        val res: Int = intent.getIntExtra("EXTRA_DATA",0)
        binding.imageView.setImageResource(R.drawable.hanabi)
    }

}


