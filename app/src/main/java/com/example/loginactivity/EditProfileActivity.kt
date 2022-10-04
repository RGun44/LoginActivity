package com.example.loginactivity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginactivity.databinding.ActivityEditProfileBinding
import com.example.loginactivity.room.User
import com.example.loginactivity.room.UserDB
import java.util.*


class EditProfileActivity : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    var itemBinding: ActivityEditProfileBinding? = null
    var sharedPreferences: SharedPreferences? = null
    private lateinit var editProfileLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemBinding = ActivityEditProfileBinding.inflate(layoutInflater)

        setContentView(itemBinding?.root)

        sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE)
        val id = sharedPreferences?.getString("id", "")
        itemBinding?.etsignup?.setText(db.userDao().getUser(id!!.toInt()).username)
        itemBinding?.etemail?.setText(db.userDao().getUser(id!!.toInt()).email)
        itemBinding?.etphone?.setText(db.userDao().getUser(id!!.toInt()).phonenumber)
        itemBinding?.etbirthdate?.setText(db.userDao().getUser(id!!.toInt()).birthdate)
        itemBinding?.etenterpass?.setText(db.userDao().getUser(id!!.toInt()).password)

        itemBinding?.etbirthdate?.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    itemBinding?.birthdate?.editText?.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)

                },
                year,
                month,
                day
            )

            dpd.show()
        }


        itemBinding?.btnSignUp?.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, LoginActivity::class.java)

            val Name: String = itemBinding?.signup?.editText?.text.toString()
            val NoTelp: String = itemBinding?.phonenumber?.editText?.text.toString()
            val Email: String = itemBinding?.emailsignup?.editText?.text.toString()
            val BirthDate: String = itemBinding?.birthdate?.editText?.text.toString()

            var checkSave = true

            if (Name.isEmpty()) {
                itemBinding?.signup?.error = "Name must be filled with text"
                checkSave = false
            }

            if (NoTelp.isEmpty()) {
                itemBinding?.phonenumber?.error = "Phone Number must be filled with text"
                checkSave = false
            }

            if (Email.isEmpty()) {
                itemBinding?.emailsignup?.error = "E-mail must be filled with text"
                checkSave = false
            }

            if (!Email.matches(Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"))) {
                itemBinding?.emailsignup?.error = "Email tidak valid"
                checkSave = false
            }

            if (BirthDate.isEmpty()) {
                itemBinding?.etemail?.error = "Birth Date must be filled with text"
                checkSave = false
            }

            if (checkSave == true) {
                setupListener()
                Toast.makeText(
                    applicationContext,
                    "Your Profile Changed",
                    Toast.LENGTH_SHORT
                ).show()
                val moveMenu = Intent(this, HomeActivity::class.java)
                startActivity(moveMenu)
            } else {
                return@OnClickListener
            }
        })
    }


    private fun setupListener() {
        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("id", "")

        db.userDao().updateUser(
            User(
                id!!.toInt(),
                itemBinding?.etsignup?.text.toString(),
                itemBinding?.etphone?.text.toString(),
                itemBinding?.etemail?.text.toString(),
                itemBinding?.etbirthdate?.text.toString(),
                db.userDao().getUser(id.toInt()).password.toString()
            )
        )
        finish()
    }

}