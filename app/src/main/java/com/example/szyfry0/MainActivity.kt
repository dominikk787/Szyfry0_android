package com.example.szyfry0

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCMorse.setOnClickListener {
            val intent = Intent(this, CMorseActivity::class.java)
            startActivity(intent)
        }

        btnDMorse.setOnClickListener {
            val intent = Intent(this, DMorseActivity::class.java)
            startActivity(intent)
        }
    }
}