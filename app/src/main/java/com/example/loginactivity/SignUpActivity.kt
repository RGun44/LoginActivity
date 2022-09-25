package com.example.loginactivity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.example.loginactivity.databinding.ActivitySignUpBinding
import com.example.loginactivity.room.Constant
import com.example.loginactivity.room.UserDB
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity(){
    val db by lazy { UserDB(this) }
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

//    private fun deleteDialog(user: UserDB){
//        val alertDialog = AlertDialog.Builder(this)
//        alertDialog.apply {
//            setTitle("Confirmation")
//            setMessage("Are You Sure to delete this data?")
//            setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss()
//            })
//            setPositiveButton("Delete", DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss()
//                CoroutineScope(Dispatchers.IO).launch {
//                    db.userDao().deleteUser(user)
//                    loadData()
//                }
//            })
//        }
//        alertDialog.show()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        loadData()
//    }
//
//    fun loadData() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val notes = db.userDao().getUser()
//            Log.d("MainActivity","dbResponse: $notes")
//            withContext(Dispatchers.Main){
//
//            }
//        }
//    }
//
//    fun setupListener() {
//        btnSignUp.setOnClickListener{
//            intentEdit(0, Constant.TYPE_CREATE)
//        }
//    }
//
//    fun intentEdit(noteId : Int, intentType: Int){
//        startActivity(
//            Intent(applicationContext, EditProfileActivity::class.java)
//                .putExtra("intent_id", noteId)
//                .putExtra("intent_type", intentType)
//        )
//    }
}