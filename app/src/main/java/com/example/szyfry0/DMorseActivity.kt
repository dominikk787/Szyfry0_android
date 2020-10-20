package com.example.szyfry0

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_d_morse.*

class DMorseActivity : AppCompatActivity() {
    private var intxt = ""
    private fun updateMorse() {
        inTextD.setText(intxt.replace(".", "\u2022").replace("-", "\u2212"))
        outTextD.setText(Morse.decode(intxt))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_d_morse)

        btnDot.setOnClickListener {
            intxt += '.'
            updateMorse()
        }
        btnLine.setOnClickListener {
            intxt += '-'
            updateMorse()
        }
        btnSlash.setOnClickListener {
            intxt += '/'
            updateMorse()
        }
        btnBack.setOnClickListener {
            intxt = intxt.dropLast(1)
            updateMorse()
        }
    }
}