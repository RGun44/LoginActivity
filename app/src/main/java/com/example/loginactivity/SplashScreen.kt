package com.example.loginactivity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager

class SplashScreen : AppCompatActivity() {
    private val myPreference = "myPref"
    private var confirm=null
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sharedPreferences = getSharedPreferences(myPreference,
            Context.MODE_PRIVATE)
        if (sharedPreferences!!.contains(confirm)) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }else{
            val editor: SharedPreferences.Editor =
                sharedPreferences!!.edit()
            editor.putString(confirm, "true")
            editor.apply()

            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

            Handler().postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000) // 3000 is the delayed time in milliseconds.
        }
    }
}