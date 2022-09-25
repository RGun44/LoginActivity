package com.example.loginactivity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.loginactivity.room.Constant
import com.example.loginactivity.room.Paket
import com.example.loginactivity.room.UserDB
import kotlinx.android.synthetic.main.activity_paket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaketActivity : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    lateinit var paketAdapt: PaketAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paket)
//        setupListener()
      //  setupRecyclerView()
    }
    // masih belum bisa recyclerview
//    private fun setupRecyclerView() {
//        paketAdapt = paketAdapter(arrayListOf(), object :
//            paketAdapter.OnAdapterListener{
//            override fun onClick(paket: Paket) {
//                Toast.makeText(applicationContext, paket.username,
//                    Toast.LENGTH_SHORT).show()
//                intentEdit(paket.id, Constant.TYPE_READ)
//            }
//            override fun onUpdate(paket: Paket) {
//                intentEdit(paket.id, Constant.TYPE_UPDATE)
//            }
//            override fun onDelete(paket: Paket) {
//                deleteDialog(paket)
//            }
//        })
//        list_paket.apply {
//            layoutManager = LinearLayoutManager(applicationContext)
//            adapter = paketAdapt
//        }
//    }

//    private fun deleteDialog(paket: Paket){
//        val alertDialog = AlertDialog.Builder(this)
//        alertDialog.apply {
//            setTitle("Confirmation")
//            setMessage("Are You Sure to delete this data?")
//            setNegativeButton("Cancel", DialogInterface.OnClickListener
//            { dialogInterface, i ->
//                dialogInterface.dismiss()
//            })
//            setPositiveButton("Delete", DialogInterface.OnClickListener
//            { dialogInterface, i ->
//                dialogInterface.dismiss()
//                CoroutineScope(Dispatchers.IO).launch {
//                    db.paketDao().deletePaket(paket)
//                    loadData()
//                }
//            })
//        }
//        alertDialog.show()
//    }
//    override fun onStart() {
//        super.onStart()
//        loadData()
//    }
//    //untuk load data yang tersimpan pada database yang sudah create data
//
//    fun loadData() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val reservasi_val = db.paketDao().getPaket()
//            Log.d("MainActivity","dbResponse: $reservasi_val")
//            withContext(Dispatchers.Main){
//                paketAdapt.setData( reservasi_val )
//            }
//        }
//    }
//    fun setupListener() {
//        button_create.setOnClickListener{
//            intentEdit(0, Constant.TYPE_CREATE)
//        }
//    }
//    //pick data dari Id yang sebagai primary key
//    fun intentEdit(reservasiId : Int, intentType: Int){
//        startActivity(
//            Intent(applicationContext, EditPaketActivity::class.java)
//                .putExtra("intent_id", reservasiId)
//                .putExtra("intent_type", intentType)
//        )
//    }
}