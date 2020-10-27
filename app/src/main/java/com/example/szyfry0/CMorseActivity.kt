package com.example.szyfry0

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_c_morse.*

class CMorseActivity : AppCompatActivity() {
    private var copyUnicode = false

    private fun updateMorse() {
        outTextC.setText(Morse.encode(inTextC.text.toString()).replace(".", "\u2022").replace("-", "\u2212"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c_morse)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        copyUnicode = preferences.getBoolean("morse_copy_unicode", false)

        inTextC.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateMorse()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_copy, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menuCopyS) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var text = outTextC.text.toString()
            if(!copyUnicode) {
                text = text.replace('\u2022', '.').replace('\u2212', '-')
            }
            clipboard.setPrimaryClip(ClipData.newPlainText("Morse", text))
            Toast.makeText(this, R.string.copy_toast, Toast.LENGTH_SHORT).show()
        } else if(item.itemId == R.id.menuPasteS) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val text = clipboard.primaryClip?.getItemAt(0)?.text?.toString()
            if(text != null && text.isNotEmpty()) {
                inTextC.setText(text)
                updateMorse()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}