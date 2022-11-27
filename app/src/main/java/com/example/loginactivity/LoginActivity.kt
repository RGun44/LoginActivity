package com.example.loginactivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.loginactivity.room.User
import com.example.loginactivity.room.UserDB
import com.google.android.material.snackbar.Snackbar
import android.content.SharedPreferences
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.loginactivity.Volley.Paket
import com.example.loginactivity.VolleyUser.AddEditProfile
import com.example.loginactivity.VolleyUser.Profile
import com.example.loginactivity.VolleyUser.UserApi
import com.example.loginactivity.databinding.ActivityLoginBinding
import com.example.loginactivity.databinding.ActivitySignUpBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class  LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var inputUsername : TextInputLayout
    private lateinit var inputPassword : TextInputLayout
    var editTextName: EditText? = null
    var editTextPassword: EditText? = null
    private lateinit var mainLayout : ConstraintLayout

    private var queue: RequestQueue? = null

    private val myPreference = "myPref"
    private val name = "username"
    private val password = "password"
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE)

        queue = Volley.newRequestQueue(this)
        inputUsername = binding.inputLayoutUsername
        inputPassword = binding.inputLayoutPassword
        editTextPassword = binding.etPassword
        editTextName = binding.etUsername
        mainLayout = binding.mainLayout

        if (sharedPreferences!!.contains(name)) {
            editTextName?.setText(sharedPreferences!!.getString(name, ""))
        }
        if (sharedPreferences!!.contains(password)) {
            editTextPassword?.setText(sharedPreferences!!.getString(password, ""))
        }

        val btnLogin = binding.btnLogin
        val btnRegistrasi = binding.btnRegistrasi

        btnRegistrasi.setOnClickListener {
            val intent = Intent(this, AddEditProfile::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener OnClickListener@{
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            cekLogin(username,password)
        }
    }

    private fun cekLogin(username: String, password: String){
        // Fungsi untuk menampilkan data user berdasarkan id
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, UserApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonData = jsonObject.getJSONArray("data")
                val profile : Array<Profile> = gson.fromJson(jsonData.toString(),Array<Profile>::class.java)

                for (profile in profile) {
                    if (profile.username == username && profile.password == password){
                        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
                        var editor = sharedPreferences?.edit()
                        editor?.putString("id", profile.id.toString())
                        editor?.apply()
                        val moveHome = Intent( this@LoginActivity, HomeActivity::class.java)
                        startActivity(moveHome)
                        finish()
                        break;
                    }
                    else if (profile==null){
                        Toast.makeText(this@LoginActivity, "Username atau Password salah", Toast.LENGTH_SHORT).show()
                    }
                }
            },  Response.ErrorListener { error ->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@LoginActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }
            }
        queue!!.add(stringRequest)
    }
}