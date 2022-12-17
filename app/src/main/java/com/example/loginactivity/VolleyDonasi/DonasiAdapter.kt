package com.example.loginactivity.VolleyDonasi

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginactivity.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class DonasiAdapter(private var donasiList: List<Donasi>, context: Context):
    RecyclerView.Adapter<DonasiAdapter.ViewHolder>(), Filterable{
    private var filteredDonasiList: MutableList<Donasi>
    private val context: Context

    init {
        filteredDonasiList = ArrayList(donasiList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_donasi, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredDonasiList.size
    }

    fun setDonasiList(donasiList: Array<Donasi>) {
        this.donasiList = donasiList.toList()
        filteredDonasiList = donasiList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Donasi = filteredDonasiList[position]

        holder.tvnamaDonatur.text = Donasi.nama_Donatur
        holder.tvjenisDonasi.text = Donasi.jenis_donasi
        holder.tvnominalDonasi.text= Donasi.nominal_Donasi
        holder.tvPembayaran.text = Donasi.pembayaran

        holder.btnDelete.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data mahasiswa ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus") { _, _ ->
                    if (context is MainDonasiActivity) Donasi.id?.let { it1 ->
                        context.deleteDonasi(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvDonasi.setOnClickListener {
            val i = Intent(context, AddEditDonasiActivity::class.java)
            i.putExtra("id", Donasi.id)
            if (context is MainDonasiActivity)
                context.startActivityForResult(i, MainDonasiActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Donasi> = java.util.ArrayList()
                if (charSequenceString.isEmpty()) {
                    filtered.addAll(donasiList)
                } else {
                    for (Donasi in donasiList) {
                        if (Donasi.nama_Donatur.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(Donasi)

                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                filteredDonasiList.clear()
                filteredDonasiList.addAll((filterResults.values as List<Donasi>))
                notifyDataSetChanged()

            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvnamaDonatur: TextView
        var tvjenisDonasi: TextView
        var tvnominalDonasi: TextView
        var tvPembayaran: TextView

        var btnDelete: ImageButton
        var cvDonasi: CardView

        init {
            tvnamaDonatur = itemView.findViewById(R.id.tv_namaDonatur)
            tvjenisDonasi= itemView.findViewById(R.id.tv_jenisDonasi)
            tvnominalDonasi = itemView.findViewById(R.id.tv_nominalDonasi)
            tvPembayaran = itemView.findViewById(R.id.tv_Pembayaran)
            btnDelete = itemView.findViewById(R.id.btn_deleteDonasi)
            cvDonasi = itemView.findViewById(R.id.cv_donasi)
        }
    }
}