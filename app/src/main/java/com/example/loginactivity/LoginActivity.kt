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
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
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
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
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
    var sharedPreferences2: SharedPreferences? = null
    private var layoutLoading: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        sharedPreferences2 = getSharedPreferences("login", Context.MODE_PRIVATE)

        queue = Volley.newRequestQueue(this)
        inputUsername = binding.inputLayoutUsername
        inputPassword = binding.inputLayoutPassword
        editTextPassword = binding.etPassword
        editTextName = binding.etUsername
        mainLayout = binding.mainLayout
        layoutLoading = findViewById(R.id.layout_loading)

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
            val editor: SharedPreferences.Editor = sharedPreferences2!!.edit()
            editor.clear()
            editor.apply()
            startActivity(intent)
        }

        btnLogin.setOnClickListener OnClickListener@{
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            cekLogin(username,password)

            YoYo.with(Techniques.Tada)
                .duration(500)
                .playOn(btnLogin);
        }

    }

    private fun cekLogin(username: String, password: String){
        // Fungsi untuk menampilkan data user berdasarkan id
        setLoading(true)

        if (inputUsername!!.toString().isEmpty()){
            Toast.makeText(this@LoginActivity, "Username tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }else if (inputPassword!!.toString().isEmpty()){
            Toast.makeText(this@LoginActivity, "Password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }else{
            val stringRequest: StringRequest =
                object : StringRequest(Method.GET, UserApi.GET_ALL_URL, Response.Listener { response ->
                    val gson = Gson()
                    val jsonObject = JSONObject(response)
                    val jsonData = jsonObject.getJSONArray("data")
                    val profile : Array<Profile> = gson.fromJson(jsonData.toString(),Array<Profile>::class.java)
                    var cek: Int = 0

                    for (profile in profile) {
                        if (profile.username == username && profile.password == password){
                            sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
                            var editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                            editor.putString("id", profile.id.toString())
                            editor.apply()
                            cek=1
                            MotionToast.createToast(this,
                                "Login Success",
                                "Selamat datang " + profile.username,
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.SHORT_DURATION,
                                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                            val moveHome = Intent( this@LoginActivity, HomeActivity::class.java)
                            startActivity(moveHome)
                            finish()
                            break;
                        }
                    }

                    if (cek==0){
                        MotionToast.createToast(this,
                            "Invalid Login",
                            "Username atau Password salah",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
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
        setLoading(false)
    }

    private fun setLoading(isLoading: Boolean){
        if(isLoading){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }
}