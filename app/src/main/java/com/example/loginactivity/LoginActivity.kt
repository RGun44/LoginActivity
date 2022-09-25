package com.example.loginactivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.loginactivity.room.User
import com.example.loginactivity.room.UserDB
import com.google.android.material.snackbar.Snackbar
import android.content.SharedPreferences
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    val db by lazy{ UserDB(this) }
    private lateinit var inputUsername : TextInputLayout
    private lateinit var inputPassword : TextInputLayout
    private lateinit var mainLayout : ConstraintLayout
    lateinit var mBundle: Bundle

    lateinit var vUsername: String
    lateinit var vPassword: String
    var sharedPreferences:SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        mainLayout = findViewById(R.id.mainLayout)

        var intent : Intent = intent
        if(intent.hasExtra("SIGNUP")) {
            getBundle()
            setText()
        }

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegistrasi = findViewById<Button>(R.id.btnRegistrasi)

        btnRegistrasi.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener OnClickListener@{
            var checkLogin = false
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if(username.isEmpty()){
                inputUsername.setError("Username must be filled with text")
                checkLogin = false
            }

            if(password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
                checkLogin = false
            }

            val UserDB: User = db.userDao().getUser(username, password)

            if (UserDB != null) {
                sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
                var editor = sharedPreferences?.edit()
                editor?.putString("id", UserDB.id.toString())
                editor?.commit()
                val moveMenu = Intent(this, HomeActivity::class.java)
                startActivity(moveMenu)
            } else {
                Snackbar.make(
                    mainLayout,
                    "Username or Password incorrect",
                    Snackbar.LENGTH_LONG
                ).show()
                return@OnClickListener
            }
            if(!checkLogin)return@OnClickListener
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    fun getBundle(){
        mBundle = intent.getBundleExtra("SIGNUP")!!
        vUsername = mBundle.getString("username")!!
        vPassword = mBundle.getString("password")!!
    }

    fun setText(){
        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputUsername.getEditText()?.setText(vUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        inputPassword.getEditText()?.setText(vPassword)
    }
}