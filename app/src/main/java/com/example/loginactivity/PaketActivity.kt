package com.example.loginactivity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginactivity.databinding.ActivityPaketBinding
import com.example.loginactivity.room.Constant
import com.example.loginactivity.room.Paket
import com.example.loginactivity.room.PaketDB
import kotlinx.android.synthetic.main.activity_paket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaketActivity : AppCompatActivity() {
    val db by lazy { PaketDB(this) }
    lateinit var paketAdapt: PaketAdapter
    private lateinit var binding: ActivityPaketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaketBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupListener()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        paketAdapt = PaketAdapter(arrayListOf(), object :
            PaketAdapter.OnAdapterListener {
            override fun onClick(paket: Paket) {
                Toast.makeText(
                    applicationContext, paket.daerahAsal,
                    Toast.LENGTH_SHORT
                ).show()
                intentEdit(paket.id, Constant.TYPE_READ)
            }

            override fun onUpdate(paket: Paket) {
                intentEdit(paket.id, Constant.TYPE_UPDATE)
            }

            override fun onDelete(paket: Paket) {
                deleteDialog(paket)
            }
        })
        list_paket.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = paketAdapt
        }
    }

    private fun deleteDialog(paket: Paket) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Are You Sure to delete this data?")
            setNegativeButton("Cancel", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            setPositiveButton("Delete", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.paketDao().deletePaket(paket)
                    loadData()
                }
            })
        }
        alertDialog.show()
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }
    //untuk load data yang tersimpan pada database yang sudah create data

    fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val reservasi_val = db.paketDao().getPakets()
            Log.d("MainActivity", "dbResponse: $reservasi_val")
            withContext(Dispatchers.Main) {
                paketAdapt.setData(reservasi_val)
            }
        }
    }

    fun setupListener() {
        button_create.setOnClickListener {
            intentEdit(0, Constant.TYPE_CREATE)
        }
    }

    //pick data dari Id yang sebagai primary key
    fun intentEdit(reservasiId: Int, intentType: Int) {
        startActivity(
            Intent(applicationContext, EditPaketActivity::class.java)
                .putExtra("intent_id", reservasiId)
                .putExtra("intent_type", intentType)
        )
    }
}