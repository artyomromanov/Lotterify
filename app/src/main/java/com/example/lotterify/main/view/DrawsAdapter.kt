package com.example.lotterify.main.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lotterify.R
import com.example.lotterify.network.DrawResult
import kotlinx.android.synthetic.main.draw_item.view.*

class DrawsAdapter(private val list: List<DrawResult>) : RecyclerView.Adapter<DrawsAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.draw_item, parent, false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.bind(position)
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        fun bind(position: Int){
            itemView.tv_date.text = list[position].date
            itemView.tv_numbers.text = list[position].numbers
        }
    }
}