package com.example.szyfry0

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_playfair.*
import kotlinx.android.synthetic.main.dialog_playfair_select_key.view.*
import kotlinx.android.synthetic.main.layout_playfair_keys_list.view.*
import java.lang.Integer.max

class PlayfairActivity : AppCompatActivity() {

    private var dbHelper: PlayfairDBHelper? = null
    private var listKeys = listOf<KeyListItem>()

    private fun updatePlayfair() {
        outTextP.setText(Playfair.crypt(inTextP.text.toString(), toggleDirP.isChecked))
    }

    class MyKeyAdapter(private val context: Context, var list: List<KeyListItem>, private val new_key: KeyListItem,
                       private val dummy_key: KeyListItem, private val listener: MyItemClicksListener? = null) : RecyclerView.Adapter<MyKeyAdapter.MyKeyViewHolder>() {
        data class MyKeyViewHolder(val view: View, val textView: TextView, val button: Button, val div: View, var pos: Int) : RecyclerView.ViewHolder(view)

        override fun getItemCount(): Int {
            return max(list.size + 1, 2)
        }
        private fun getItem(pos: Int): KeyListItem {
            return if(list.isEmpty() && pos == 0) dummy_key
                    else if(pos >= list.size) new_key
                    else list[pos]
        }
        override fun onBindViewHolder(holder: MyKeyViewHolder, position: Int) {
            holder.pos = position
            holder.textView.text = getItem(position).name
            holder.view.setOnClickListener { listener?.onClickListener(it, position, getItem(position)) }
            //holder.view.setOnLongClickListener { listener?.onLongClickListener(it, position, getItem(position)) ?: false}
            if(getItem(position).id >= 0) holder.button.setOnClickListener { listener?.onButtonClickListener(it, position, getItem(position)) }
            else holder.button.visibility = View.INVISIBLE
            if(position == 0) holder.div.visibility = View.INVISIBLE
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyKeyViewHolder {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.layout_playfair_keys_list, parent, false)
            return MyKeyViewHolder(view, view.text1, view.btn, view.div, -1)
        }

        interface MyItemClicksListener {
            fun onClickListener(view: View, pos: Int, item: KeyListItem)
            //fun onLongClickListener(view: View, pos: Int, item: KeyListItem): Boolean
            fun onButtonClickListener(view: View, pos: Int, item: KeyListItem)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playfair)

        dbHelper = PlayfairDBHelper(this)

        genKeyList()
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
        btnSelKeyP.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            var dialog: AlertDialog? = null
            val inflater = this.layoutInflater
            val v = inflater.inflate(R.layout.dialog_playfair_select_key, null)
            val viewManager = LinearLayoutManager(this)
            val viewAdapter = MyKeyAdapter(this, listKeys, KeyListItem(-2, getString(R.string.new_key)), KeyListItem(-1, ""),
                object : MyKeyAdapter.MyItemClicksListener {
                    override fun onClickListener(view: View, pos: Int, item: KeyListItem) {
                        when(item.id) {
                            -1->{
                                textKeyNameP.text = item.name
                            }
                            -2 -> {
                                val intent = Intent(this@PlayfairActivity, PlayfairKeyActivity::class.java)
                                startActivityForResult(intent, 1)
                            }
                            in 0..Int.MAX_VALUE -> {
                                textKeyNameP.text = item.name
                                Playfair.Key.setAlphabet(item.abId)
                                Playfair.Key.setKey(item.key)
                                updatePlayfair()
                            }
                        }
                        dialog?.dismiss()
                    }
                    override fun onButtonClickListener(view: View, pos: Int, item: KeyListItem) {
                        println("button click $view at $pos on $item")
                        val build = AlertDialog.Builder(ContextThemeWrapper(this@PlayfairActivity, android.R.style.Theme_Material_Dialog_Alert))
                        build.apply {
                            setPositiveButton("Edytuj") { dialogInterface, i ->
                                println("edit key $item $dialogInterface $i")
                                dialog?.dismiss()
                            }
                            setNegativeButton("Usuń") { dialogInterface, i ->
                                println("delete key $item $dialogInterface $i")
                                dialog?.dismiss()
                            }
                            setNeutralButton("Anuluj") { dialogInterface, i ->
                                println("cancel key $item $dialogInterface $i")
                            }
                            setTitle("Zmień klucz")
                        }
                        build.create().show()
                    }
                })
            v.recyclerPD.apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter
            }
            builder.setView(v).setTitle("Wybierz klucz")
            dialog = builder.create()
            dialog.show()
        }
    }

    private fun genKeyList() {
        listKeys = dbHelper?.getAllKeys() ?: listOf()
        println(listKeys)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1) {
            println("result key ${data?.getStringExtra("KeyName")} ${data?.getIntExtra("Alphabet", 0)} ${data?.getStringExtra("KeyV")}")
            if(resultCode == 1 && data != null && data.hasExtra("KeyName") && data.hasExtra("Alphabet") && data.hasExtra("KeyV")) {
                val key = KeyListItem(listKeys[listKeys.size - 2].id + 1,
                    data.getStringExtra("KeyName") ?: "",
                    data.getIntExtra("Alphabet", 0),
                    data.getStringExtra("KeyV") ?: "")
                if (data.hasExtra("Id")) {
                    key.id = data.getIntExtra("Id", 0)
                    dbHelper?.updateKey(key)
                } else {
                    dbHelper?.addKey(key)
                }
                genKeyList()
                println("result ${listKeys.size} ${key.id} ${key.name} ${key.key}")
                Playfair.Key.setAlphabet(key.abId)
                Playfair.Key.setKey(key.key)
                textKeyNameP.text = key.name
                updatePlayfair()
            }
        }
    }
}