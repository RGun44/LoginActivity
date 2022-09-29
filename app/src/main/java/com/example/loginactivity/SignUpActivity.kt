package com.example.loginactivity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.loginactivity.databinding.ActivitySignUpBinding
import com.example.loginactivity.room.User
import com.example.loginactivity.room.UserDB
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity(){
    val db by lazy { UserDB(this) }
    private lateinit var binding: ActivitySignUpBinding
    private var userId: Int = 0

    private val CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        createNotificationChannel()
        title = "User Login"

        var btnClear = binding.btnClear
        var btnSignUp = binding.btnSignUp
        var username = binding.etsignup
        var email = binding.etemail
        var phonenumber = binding.etphone
        var birthdate = binding.etbirthdate
        var password = binding.etenterpass


        btnClear.setOnClickListener {
            username.setText("")
            email.setText("")
            phonenumber.setText("")
            birthdate.setText("")
            password.setText("")

            Snackbar.make(mainLayout, "Text Cleared Success", Snackbar.LENGTH_LONG).show()
        }

        btnSignUp.setOnClickListener(View.OnClickListener {
            sendNotification1()
            var checkSignUp = false
            val getUsername: String = username.text.toString()
            val getPassword: String = password.text.toString()
            val getBirthdate: String = birthdate.text.toString()
            val getEmail: String = email.text.toString()
            val getPhoneNumber: String = phonenumber.text.toString()


            if (getUsername.isEmpty()) {
                username.error = "Username must be filled with text"
                checkSignUp = false
            } else if (getBirthdate.isEmpty()) {
                birthdate.error = "Birth Date must be filled with text"
                checkSignUp = false
            } else if (getEmail.isEmpty()) {
                email.error = "Email must be filled with text"
                checkSignUp = false
            } else if (getPhoneNumber.isEmpty()) {
                phonenumber.error = "Phone Number must be filled with text"
                checkSignUp = false
            } else if (getPassword.isEmpty()) {
                password.error = "Password must be filled with text"
                checkSignUp = false
            } else {
                checkSignUp = true
            }

            if (!checkSignUp) return@OnClickListener
            val moveHome = Intent(this@SignUpActivity, LoginActivity::class.java)

            val mBundle = Bundle()
            mBundle.putString("username", getUsername)
            mBundle.putString("password", getPassword)
            moveHome.putExtra("SIGNUP", mBundle)

            setupListener()
            startActivity(moveHome)
        })
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 =  NotificationChannel(CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT ).apply{
                description =descriptionText
            }


            val notificationManager : NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    fun setupListener() {
        var username = binding.etsignup
        var email = binding.etemail
        var phonenumber = binding.etphone
        var birthdate = binding.etbirthdate
        var password = binding.etenterpass

        CoroutineScope(Dispatchers.IO).launch {
            db.userDao().addUser(
                User(
                    0,
                    username.text.toString(),
                    password.text.toString(),
                    email.text.toString(),
                    phonenumber.text.toString(),
                    birthdate.text.toString()
                )
            )
            finish()
        }
    }

    private fun sendNotification1(){
        val intent: Intent = Intent(this, LoginActivity::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0,intent,0)

        val broadcastIntent: Intent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage","Selamat Datang " + binding.etsignup.text.toString())
        val actionIntent = PendingIntent.getBroadcast(this,0,broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val picture = BitmapFactory.decodeResource(resources,R.drawable.logo)
        val builder = NotificationCompat.Builder(this,CHANNEL_ID_1)
            .setSmallIcon(R.drawable.logo)
            .setContentText("Berhasil Register")
//                Big Picture Style
            .setLargeIcon(picture)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigLargeIcon(null)
                .bigPicture(picture))
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.BLACK)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "Pesan", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId1,builder.build())
        }
    }

}