package com.example.loginactivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
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

}