package com.example.loginactivity.VolleyUser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.loginactivity.HomeActivity
import com.example.loginactivity.LoginActivity
import com.example.loginactivity.R
import com.example.loginactivity.Volley.Paket
import com.example.loginactivity.Volley.PaketApi
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class AddEditProfile : AppCompatActivity() {

    private var etUsername: EditText? = null
    private var etPassword: EditText? = null
    private var etEmail: EditText? = null
    private var etPhonenumber: EditText? = null
    private var etBirthdate: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_profile)

        //Pendeklarasian request queue
        queue = Volley.newRequestQueue(this)
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        etEmail = findViewById(R.id.et_email)
        etPhonenumber = findViewById(R.id.et_phonenumber)
        etBirthdate = findViewById(R.id.et_birthdate)
        layoutLoading = findViewById(R.id.layout_loading)

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener{finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)

        val tvLogo =findViewById<TextView>(R.id.tvLogo)

        val id = intent.getLongExtra("id", -1)
        if(id== -1L){
            tvLogo.setText("Sign Up")
            btnSave.setOnClickListener {
                createUser()

                val mBundle = Bundle()
                mBundle.putString("username", etUsername?.text.toString())
                mBundle.putString("password", etPassword?.text.toString())
            }
        }else{
            tvLogo.setText("Edit Profile")
            getUserById(id)

            btnSave.setOnClickListener { updateUser(id) }
        }
    }

    private fun getUserById(id: Long){
        // Fungsi untuk menampilkan data user berdasarkan id
        setLoading(true)
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, UserApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val profile = gson.fromJson(response, Profile::class.java)

                etUsername!!.setText(profile.username)
                etPassword!!.setText(profile.password)
                etEmail!!.setText(profile.email)
                etPhonenumber!!.setText(profile.phonenumber)
                etBirthdate!!.setText(profile.birthdate)

                Toast.makeText(this@AddEditProfile, "Data berhasil diambil", Toast.LENGTH_SHORT).show()
                setLoading(false)
            },  Response.ErrorListener { error ->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditProfile,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@AddEditProfile, e.message, Toast.LENGTH_SHORT).show()
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

    private fun createUser(){
        setLoading(true)

        val profile = Profile(
            etUsername!!.text.toString(),
            etPassword!!.text.toString(),
            etEmail!!.text.toString(),
            etPhonenumber!!.text.toString(),
            etBirthdate!!.text.toString(),
        )

        val stringRequest: StringRequest =
            object: StringRequest(Method.POST, UserApi.ADD_URL, Response.Listener { response->
                val gson = Gson()
                val profile = gson.fromJson(response, Profile::class.java)

                if(profile!=null)
                    Toast.makeText(this@AddEditProfile, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent(this@AddEditProfile, LoginActivity::class.java)

                val mBundle = Bundle()
                mBundle.putString("username", etUsername?.text.toString())
                mBundle.putString("password", etPassword?.text.toString())
                returnIntent.putExtra("SIGNUP",mBundle)

                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditProfile,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@AddEditProfile, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers

                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(profile)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        // Menambahkan request ke request queue
        queue!!.add(stringRequest)
    }

    private fun updateUser(id: Long){
        setLoading(true)

        val profile = Profile(
            etUsername!!.text.toString(),
            etPassword!!.text.toString(),
            etEmail!!.text.toString(),
            etPhonenumber!!.text.toString(),
            etBirthdate!!.text.toString(),
        )

        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, UserApi.UPDATE_URL + id, Response.Listener{ response ->
                val gson = Gson()

                val paket = gson.fromJson(response, Profile::class.java)

                if(paket != null)
                    Toast.makeText(this@AddEditProfile, "Data Berhasil Diupdate",Toast.LENGTH_SHORT).show()
                val returnIntent = Intent(this@AddEditProfile, HomeActivity::class.java)
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener{ error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditProfile,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@AddEditProfile, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                val gson = Gson()
                val requestBody = gson.toJson(profile)
                return requestBody.toByteArray(StandardCharsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        queue!!.add(stringRequest)

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