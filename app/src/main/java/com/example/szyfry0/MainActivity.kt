package com.example.szyfry0

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val array = resources.obtainTypedArray(R.array.test_array)
        if(array.length() > 0) {
            val id = array.getResourceId(0, 0)
            if(id > 0) {
                val str = resources.getString(id)
                val text = "id: $id\nstr: $str"
                Toast.makeText(this, text, Toast.LENGTH_LONG).show()
                println(text)
            }
        }

        btnCMorse.setOnClickListener {
            val intent = Intent(this, CMorseActivity::class.java)
            startActivity(intent)
        }

        btnDMorse.setOnClickListener {
            val intent = Intent(this, DMorseActivity::class.java)
            startActivity(intent)
        }

        btnPlayfair.setOnClickListener {
            val intent = Intent(this, PlayfairActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menuSettingM) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        } else if(item.itemId == R.id.menuOthersM) {
            val intent = Intent(this, OthersActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}