package com.example.szyfry0

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_playfair.*
import kotlinx.android.synthetic.main.layout_playfair_keys_list.view.*
import java.lang.Integer.max

class PlayfairActivity : AppCompatActivity() {

    private var context: Context = this
    private var spnKeyId = 0
    private var dbHelper: PlayfairDBHelper? = null

    private var listKeys = listOf<KeyListItem>()

    private fun updatePlayfair() {
        outTextP.setText(Playfair.crypt(inTextP.text.toString(), toggleDirP.isChecked))
    }

    class MyKeyAdapter(private val c: Context, var l: List<KeyListItem>, private val new_key: KeyListItem, private val dummy_key: KeyListItem, val listener: MyOnClickListener?) : BaseAdapter() {
        override fun getCount(): Int {
            return max(l.size + 1, 2)
        }
        override fun getItem(p0: Int): Any {
            return if(p0 == 0 && l.isEmpty()) dummy_key
                    else if(p0 >= max(l.size, 1)) new_key
                    else l[p0]
        }
        override fun getItemId(p0: Int): Long {
            return (getItem(p0) as KeyListItem).id.toLong()
        }
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val inflater = c.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var v = p1
            if(v == null) {
                v = inflater.inflate(R.layout.layout_playfair_keys_list, p2, false)
                val holder = MyViewHolder(v.findViewById(R.id.text1), v.findViewById(R.id.btn))
                v?.tag = holder
            }
            val holder = v?.tag as MyViewHolder
            holder.textview.text = (getItem(p0) as KeyListItem).name
            holder.button.setOnClickListener { listener?.OnClick(p1 ?: View(c), p0) }
            println("get view $p0 $p1 $p2 ${getItem(p0)}")
            return v
        }
        private data class MyViewHolder(var textview: TextView, var button: Button)
        interface MyOnClickListener {
            fun OnClick(view: View, pos: Int)
        }
    }

    var adapter: MyKeyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playfair)

        dbHelper = PlayfairDBHelper(this)

        //keyAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listKeys)
        adapter = MyKeyAdapter(this, listKeys, KeyListItem(-2, this.getString(R.string.new_key)), KeyListItem(-1, ""), object : MyKeyAdapter.MyOnClickListener {
            override fun OnClick(view: View, pos: Int) {
                println("button in $view at $pos")
                spnKeyP.setSelection(pos, true)
            }

        })
        genKeyList()
        spnKeyP.adapter = adapter
        spnKeyP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                println("result $p2")
                val id = spnKeyP.selectedItemId.toInt()
                if(id == -2) {
                    val intent = Intent(context, PlayfairKeyActivity::class.java)
                    startActivityForResult(intent, 1)
                } else {
                    val item = listKeys[id - 1]
                    spnKeyId = p2
                    if(item.abId >= 0) {
                        Playfair.Key.setAlphabet(item.abId)
                        Playfair.Key.setKey(item.key)
                        updatePlayfair()
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        inTextP.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updatePlayfair()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        toggleDirP.setOnClickListener {
            updatePlayfair()
        }
    }

    private fun genKeyList() {
        val list = dbHelper?.getAllKeys() ?: listOf()
        println(list)
        adapter?.l = list
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
                        data.getIntExtra("Alphabet", 0),
                        data.getStringExtra("KeyV") ?: "")
                dbHelper?.addKey(key)
                genKeyList()
                spnKeyP.setSelection(listKeys.size - 2)
                spnKeyId = listKeys.size - 2
                println("result ${listKeys.size} ${key.id} ${key.name} ${key.key}")
                updatePlayfair()
            } else {
                spnKeyP.setSelection(spnKeyId)
            }
        }
    }
}