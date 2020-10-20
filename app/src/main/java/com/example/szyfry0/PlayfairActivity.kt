package com.example.szyfry0

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PlayfairActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playfair)
        Playfair.Key.setAlphabet(0)
        Playfair.Key.setKey("tajny klucz abc")
    }
}