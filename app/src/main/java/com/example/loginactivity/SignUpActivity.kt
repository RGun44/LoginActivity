package com.example.loginactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.example.loginactivity.databinding.ActivitySignUpBinding
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.w3c.dom.Text

class SignUpActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySignUpBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setTitle("User Login")

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
            val getUsername: String = username.getText().toString()
            val getPassword: String = password.getText().toString()
            val getBirthdate: String = birthdate.getText().toString()
            val getEmail: String = email.getText().toString()
            val getPhoneNumber: String = phonenumber.getText().toString()


            if(getUsername.isEmpty()){
                username.setError("Username must be filled with text")
                checkSignUp = false
            }
            else if(getBirthdate.isEmpty()){
                birthdate.setError("Birth Date must be filled with text")
                checkSignUp = false
            }
            else if(getEmail.isEmpty()){
                email.setError("Email must be filled with text")
                checkSignUp = false
            }
            else if(getPhoneNumber.isEmpty()){
                phonenumber.setError("Phone Number must be filled with text")
                checkSignUp = false
            }
            else if(getPassword.isEmpty()){
                password.setError("Password must be filled with text")
                checkSignUp = false
            }
            else {
                checkSignUp = true
            }

            if(!checkSignUp)return@OnClickListener
            val moveHome = Intent( this@SignUpActivity, LoginActivity::class.java)

            val mBundle = Bundle()
            mBundle.putString("username" , getUsername)
            mBundle.putString("password" , getPassword)
            moveHome.putExtra("SIGNUP", mBundle)

            startActivity(moveHome)
        })
    }
}