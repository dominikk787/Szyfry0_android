package com.example.szyfry0

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.AdapterView
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_playfair_key.*
import kotlinx.android.synthetic.main.layout_playfair_key_grid.view.*
import java.util.*

class PlayfairKeyActivity : AppCompatActivity() {
    class CharAdapter(private val c: Context) : BaseAdapter() {
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val inflater = c.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val v = p1 ?: inflater.inflate(R.layout.layout_playfair_key_grid, p2, false)
            v.textV.text = getItem(p0) as String
            return v
        }
        override fun getItem(p0: Int): Any? {
            return Playfair.Key.get(p0.rem(Playfair.Key.getAlphabet().size), p0 / (Playfair.Key.getAlphabet().size)).toString()
        }
        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }
        override fun getCount(): Int {
            return (Playfair.Key.getAlphabet().size) * (Playfair.Key.getAlphabet().size)
        }
    }

    var id: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playfair_key)

        val extras = intent.extras
        if(extras != null) {
            id = extras.getInt("Id", -1)
            spnAbPK.setSelection(extras.getInt("Alphabet", 0))
            editNamePK.setText(extras.getString("KeyName", ""))
            editKeyPK.setText(extras.getString("KeyV", ""))
        }

        fun onKeyChange() {
            println("test")
            val id = spnAbPK.selectedItemId
            println(id)
            Playfair.Key.setAlphabet(id.toInt())
            Playfair.Key.setKey(editKeyPK.text.toString())
            gridKeyPK.numColumns = Playfair.Key.getAlphabet().size
            gridKeyPK.adapter = CharAdapter(this)
        }
        spnAbPK.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                onKeyChange()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        editKeyPK.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onKeyChange()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_playfair_key, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menuSavePK) {
            val intent = Intent()
            intent.putExtra("KeyName", editNamePK.text.toString().toUpperCase(Locale.getDefault()))
            intent.putExtra("Alphabet", spnAbPK.selectedItemId.toInt())
            intent.putExtra("KeyV", Playfair.Key.get())
            if(id >= 0) intent.putExtra("Id", id)
            setResult(1, intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        setResult(0)
        super.onBackPressed()
    }
}