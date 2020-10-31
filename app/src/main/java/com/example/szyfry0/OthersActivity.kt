package com.example.szyfry0

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_others.*
import kotlinx.android.synthetic.main.layout_others_recycler.view.*

class OthersActivity : AppCompatActivity() {

    data class MyOtherCipherItem(val title: String, val img: Drawable?, val description: String)
    class MyOtherCipherAdapter(private val context: Context, var list: List<MyOtherCipherItem>) : RecyclerView.Adapter<MyOtherCipherAdapter.MyViewHolder>() {
        data class MyViewHolder(val view: View, val text1: TextView, val text2: TextView, var pos: Int) : RecyclerView.ViewHolder(view)

        override fun getItemCount(): Int = list.size
        private fun getItem(pos: Int): MyOtherCipherItem = list[pos]
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.layout_others_recycler, parent, false)
            println("create")
            return MyViewHolder(view, view.text1, view.text2, -1)
        }
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.pos = position
            holder.text1.text = getItem(position).title
            if(context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                holder.text2.setCompoundDrawablesWithIntrinsicBounds(getItem(position).img, null, null, null)
            } else {
                holder.text2.setCompoundDrawablesWithIntrinsicBounds(null, getItem(position).img, null, null)
            }
            holder.text2.text = getItem(position).description
            println("bind $holder")
        }
    }

    var list = listOf<MyOtherCipherItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_others)

        list = listOf(MyOtherCipherItem(getString(R.string.test0), resources.getDrawable(R.drawable.ic_launcher_background, null), getString(R.string.test1)))
        recyclerO.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@OthersActivity)
            adapter = MyOtherCipherAdapter(this@OthersActivity, list)
        }
    }
}