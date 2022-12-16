package com.example.loginactivity.VolleyDriver

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

class DriverAdapter(private var driverList: List<Driver>, context: Context):
    RecyclerView.Adapter<DriverAdapter.ViewHolder>(), Filterable{
        private var filtereddriverList: MutableList<Driver>
        private val context: Context

        init {
            filtereddriverList = ArrayList(driverList)
            this.context = context
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_driver, parent, false)

            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return filtereddriverList.size
        }

    fun setDriverList(driverList: Array<Driver>) {
        this.driverList = driverList.toList()
        filtereddriverList = driverList.toMutableList()
    }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val Driver = filtereddriverList[position]

            holder.tvnamaDriver.text = Driver.nama_driver
            holder.tvusiaDriver.text = Driver.usia_driver
            holder.tvnoTelp.text= Driver.noTelp
            holder.tvalamat.text = Driver.alamat

            holder.btnDelete.setOnClickListener {
                val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
                materialAlertDialogBuilder.setTitle("Konfirmasi")
                    .setMessage("Apakah anda yakin ingin menghapus data mahasiswa ini?")
                    .setNegativeButton("Batal", null)
                    .setPositiveButton("Hapus") { _, _ ->
                        if (context is MainDriverActivity) Driver.id?.let { it1 ->
                            context.deleteDriver(
                                it1
                            )
                        }
                    }
                    .show()
            }
            holder.cvDriver.setOnClickListener {
                val i = Intent(context, AddEditDriverActivity::class.java)
                i.putExtra("id", Driver.id)
                if (context is MainDriverActivity)
                    context.startActivityForResult(i, MainDriverActivity.LAUNCH_ADD_ACTIVITY)
            }
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(charSequence: CharSequence): FilterResults {
                    val charSequenceString = charSequence.toString()
                    val filtered: MutableList<Driver> = java.util.ArrayList()
                    if (charSequenceString.isEmpty()) {
                        filtered.addAll(driverList)
                    } else {
                        for (Driver in driverList) {
                            if (Driver.nama_driver.lowercase(Locale.getDefault())
                                    .contains(charSequenceString.lowercase(Locale.getDefault()))
                            ) filtered.add(Driver)

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
                    filtereddriverList.clear()
                    filtereddriverList.addAll((filterResults.values as List<Driver>))
                    notifyDataSetChanged()

                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var tvnamaDriver: TextView
            var tvusiaDriver: TextView
            var tvnoTelp: TextView
            var tvalamat: TextView

            var btnDelete: ImageButton
            var cvDriver: CardView

            init {
                tvnamaDriver = itemView.findViewById(R.id.tv_namaDriver)
                tvusiaDriver= itemView.findViewById(R.id.tv_usiaDriver)
                tvnoTelp = itemView.findViewById(R.id.tv_noTelp)
                tvalamat = itemView.findViewById(R.id.tv_alamat)
                btnDelete = itemView.findViewById(R.id.btn_delete)
                cvDriver = itemView.findViewById(R.id.cv_paket)
            }
        }
    }