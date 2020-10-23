package com.example.szyfry0

import android.content.Context
import android.content.Intent
import android.database.DataSetObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_playfair.*

class PlayfairActivity : AppCompatActivity() {

    private var context: Context = this
    private var spnKeyId = 0

    data class KeyListItem(val id: Int, var name: String, var ab: Playfair.Key.Alphabet = Playfair.Key.Alphabet("", 0), val key: String = "") {
        override fun toString(): String = name
    }

    private var listKeys = MutableList<KeyListItem>(0) {
        KeyListItem(-1, "")
    }
    private var keyAdapter: ArrayAdapter<KeyListItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playfair)

        keyAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listKeys)
        genKeyList()
        spnKeyP.adapter = keyAdapter
        spnKeyP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                println("result $p2")
                val item = listKeys[spnKeyP.selectedItemId.toInt()]
                if(item.id == -2) {
                    val intent = Intent(context, PlayfairKeyActivity::class.java)
                    startActivityForResult(intent, 1)
                } else spnKeyId = p2
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun genKeyList() {
        val list = listKeys.filter {it.id >= 0}.toMutableList()
        println(list)
        if(list.size == 0)
            list.add(KeyListItem(-1, ""))
        //list.add(KeyListItem(1, "test 2", Playfair.Key.getAlphabet(1), "test abc123"))
        list.add(KeyListItem(-2, applicationContext.getString(R.string.new_key)))
        println(list)
        keyAdapter?.clear()
        keyAdapter?.addAll(list)
        listKeys = list
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1) {
            println("result key ${data?.getStringExtra("KeyName")} ${data?.getIntExtra("Alphabet", 0)} ${data?.getStringExtra("KeyV")}")
            if(resultCode == 1 && data != null && data.hasExtra("KeyName") && data.hasExtra("Alphabet") && data.hasExtra("KeyV")) {
                println("result $spnKeyId")
                val key = KeyListItem(listKeys[listKeys.size - 2].id + 1,
                        data.getStringExtra("KeyName") ?: "",
                        Playfair.Key.getAlphabet(data.getIntExtra("Alphabet", 0)),
                        data.getStringExtra("KeyV") ?: "")
                listKeys.add(key)
                genKeyList()
                spnKeyP.setSelection(listKeys.size - 2)
                spnKeyId = listKeys.size - 2
                println("result ${listKeys.size} ${key.id} ${key.name} ${key.key}")
            } else {
                spnKeyP.setSelection(spnKeyId)
            }
        }
    }
}