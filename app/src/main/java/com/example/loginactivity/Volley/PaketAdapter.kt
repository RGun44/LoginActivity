package com.example.loginactivity.Volley

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

class PaketAdapter(private var paketList: List<Paket>,context: Context):
    RecyclerView.Adapter<PaketAdapter.ViewHolder>(), Filterable {
    private var filteredPaketList: MutableList<Paket>
    private val context: Context

    init {
        filteredPaketList = ArrayList(paketList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_paket, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredPaketList.size
    }

    fun setPaketList(paketList: Array<Paket>) {
        this.paketList = paketList.toList()
        filteredPaketList = paketList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val paket = filteredPaketList[position]

        holder.tvDaerahAsal.text = paket.daerah_asal
        holder.tvDaerahTujuan.text = paket.daerah_tujuan
        holder.tvBeratPaket.text = paket.berat_paket
        holder.tvKecepatan.text = paket.kecepatan

        holder.btnDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data mahasiswa ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){_, _ ->
                    if(context is MainActivity) paket.id?.let{ it1 ->
                        context.deletePaket(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvPaket.setOnClickListener {
            val i = Intent(context, AddEditActivity::class.java)
            i.putExtra("id", paket.id)
            if (context is MainActivity)
                context.startActivityForResult(i, MainActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Paket> = java.util.ArrayList()
                if (charSequenceString.isEmpty()) {
                    filtered.addAll(paketList)
                } else {
                    for (paket in paketList) {
                        if (paket.daerah_asal.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(paket)

                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredPaketList.clear()
                filteredPaketList.addAll((filterResults.values as List<Paket>))
                notifyDataSetChanged()

            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvId: TextView
        var tvDaerahAsal: TextView
        var tvDaerahTujuan: TextView
        var tvBeratPaket: TextView
        var tvKecepatan: TextView
        var btnDelete: ImageButton
        var cvPaket: CardView

        init{
            tvId = itemView.findViewById(R.id.tv_id)
            tvDaerahAsal = itemView.findViewById(R.id.tv_daerahAsal)
            tvDaerahTujuan = itemView.findViewById(R.id.tv_daerahTujuan)
            tvBeratPaket = itemView.findViewById(R.id.tv_beratPaket)
            tvKecepatan = itemView.findViewById(R.id.tv_kecepatan)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvPaket = itemView.findViewById(R.id.cv_paket)
        }
    }
}