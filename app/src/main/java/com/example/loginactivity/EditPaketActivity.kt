package com.example.loginactivity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.loginactivity.databinding.ActivityEditPaketBinding
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
    private var binding: ActivityEditPaketBinding? = null
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val CHANNEL_ID_2 = "channel_notification_02"
    private val notificationId1 = 101
    private val notificationId2 = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPaketBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setupView()
        createNotificationChannel()
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
            sendNotification1()
            sendNotification2()
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
            val paket = db.paketDao().getPaket(paketId)[0]
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

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Descriptions"

            val channel1 = NotificationChannel(CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }
            val channel2 = NotificationChannel(CHANNEL_ID_2, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
            notificationManager.createNotificationChannel(channel2)
        }
    }

    private fun sendNotification1(){
        val intent : Intent = Intent(this,EditPaketActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val broadcastIntent : Intent = Intent(this,NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", binding?.editTujuan?.text.toString())
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this,CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_baseline_looks_one_24)
            .setContentTitle(binding?.editAsal?.text.toString())
            .setContentText(binding?.editTujuan?.text.toString())
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setStyle(NotificationCompat.BigTextStyle()
                .setBigContentTitle("Register Sukses")
                .bigText("Pesanan anda berhasil diregistrasi, mohon lanjut ke bagian pembayarannya")
                .setSummaryText("dari PaketIn")
            )
            .setColor(Color.BLUE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher,"Toast",actionIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId1,builder.build())
        }
    }

    private fun sendNotification2(){
        val intent : Intent = Intent(this,EditPaketActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val broadcastIntent : Intent = Intent(this,NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", binding?.editTujuan?.text.toString())
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this,CHANNEL_ID_2)
            .setSmallIcon(R.drawable.ic_baseline_looks_two_24)
            .setContentTitle(binding?.editAsal?.text.toString())
            .setContentText(binding?.editTujuan?.text.toString())
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setStyle(NotificationCompat.InboxStyle()
                .setBigContentTitle("List Data yang diregister")
                .addLine(binding?.editAsal?.text.toString())
                .addLine(binding?.editAsal?.text.toString())
                .addLine(binding?.editAsal?.text.toString())
                .addLine(binding?.editAsal?.text.toString())
            )
            .setColor(Color.BLUE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher,"Toast",actionIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId2,builder.build())
        }
    }
}