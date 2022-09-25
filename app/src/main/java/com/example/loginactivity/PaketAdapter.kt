package com.example.loginactivity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loginactivity.room.Paket

abstract class PaketAdapter (private val paket: ArrayList<Paket>, private val listener: OnAdapterListener):
    RecyclerView.Adapter<PaketAdapter.paketViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            paketViewHolder {
        return paketViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.activity_paket_adapter,parent, false)
        )
    }

    override fun getItemCount() = paket.size
    inner class paketViewHolder( val view: View) :
        RecyclerView.ViewHolder(view)
    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Paket>){
        paket.clear()
        paket.addAll(list)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(note: Paket)
        fun onUpdate(note: Paket)
        fun onDelete(note: Paket)
    }

}