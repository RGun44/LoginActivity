package com.example.loginactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginactivity.Volley.entity.Daerah

class RVDaerahAdapter (private val data: Array<Daerah>) : RecyclerView.Adapter<RVDaerahAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_daerah, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = data[position]
        holder.tvDaerah.text = currentItem.name
        holder.tvJarak.text = "${currentItem.jarak}"
        holder.tvOngkir.text = "${currentItem.est_ongkir}"
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDaerah: TextView = itemView.findViewById(R.id.nama_daerah)
        val tvJarak: TextView = itemView.findViewById(R.id.jarak)
        val tvOngkir: TextView = itemView.findViewById(R.id.ongkir)
    }
}
