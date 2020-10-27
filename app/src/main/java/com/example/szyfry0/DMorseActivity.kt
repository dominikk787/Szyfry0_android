package com.example.szyfry0

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_d_morse.*

class DMorseActivity : AppCompatActivity() {
    private var intxt = ""
    private var copyUnicode = false
    private fun updateMorse() {
        inTextD.setText(intxt.replace(".", "\u2022").replace("-", "\u2212"))
        outTextD.setText(Morse.decode(intxt))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_d_morse)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        copyUnicode = preferences.getBoolean("morse_copy_unicode", false)

        intxt = savedInstanceState?.getString("InTxt", "") ?: ""

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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("InTxt", intxt)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_copy, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menuCopyS) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("From Morse", outTextD.text.toString()))
            Toast.makeText(this, R.string.copy_toast, Toast.LENGTH_SHORT).show()
        } else if(item.itemId == R.id.menuPasteS) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var text = clipboard.primaryClip?.getItemAt(0)?.text?.toString()
            if(text != null && text.isNotEmpty()) {
                if(copyUnicode) {
                    text = text.replace('\u2022', '.').replace('\u2212', '-')
                }
                text = text.replace("[^./-]".toRegex(), "")
                intxt = text
                updateMorse()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}