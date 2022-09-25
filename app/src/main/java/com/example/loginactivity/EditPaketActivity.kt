package com.example.loginactivity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.loginactivity.room.Constant
import com.example.loginactivity.room.Paket
import com.example.loginactivity.room.PaketDB
import kotlinx.android.synthetic.main.activity_edit_paket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPaketActivity : AppCompatActivity() {
    val db by lazy { PaketDB(this) }
    private var paketId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_paket)
        setupView()
        setupListener()
    }

    fun setupView() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType) {
            Constant.TYPE_CREATE -> {
                button_update.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                button_save.visibility = View.GONE
                button_update.visibility = View.GONE
                getPaket()
            }
            Constant.TYPE_UPDATE -> {
                button_save.visibility = View.GONE
                getPaket()
            }
        }
    }

    private fun setupListener() {
        button_save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.paketDao().addPaket(
                    Paket(
                        0,
                        edit_asal.text.toString(),
                        edit_tujuan.text.toString(),
                        Integer.parseInt(edit_bobot.text.toString()),
                        edit_pilihan.text.toString()
                    )
                )
                finish()
            }
        }

        button_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.paketDao().updatePaket(
                    Paket(
                        paketId,
                        edit_asal.text.toString(),
                        edit_tujuan.text.toString(),
                        Integer.parseInt(edit_bobot.text.toString()),
                        edit_pilihan.text.toString()
                    )
                )
                finish()
            }
        }
    }

    fun getPaket() {
        paketId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val paket = db.paketDao().getPaket()[0]
            edit_asal.setText(paket.daerahAsal)
            edit_tujuan.setText(paket.daerahTujuan)
            edit_bobot.setText(paket.beratPaket.toString())
            edit_pilihan.setText(paket.kecepatan)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}