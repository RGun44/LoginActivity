package com.example.loginactivity.VolleyDriver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.loginactivity.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_edit_driver.*
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class AddEditDriverActivity : AppCompatActivity() {
    private var etId: EditText? = null
    private var etnamaDriver: EditText? = null
    private var etusiaDriver: EditText? = null
    private var etnoTelp: EditText? = null
    private var edAlamat: EditText? = null
    private var layoutLoading: LinearLayout? = null


    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_driver)

        //Pendeklarasian request queue
        queue = Volley.newRequestQueue(this)
        etId = findViewById(R.id.et_id)
        etnamaDriver = findViewById(R.id.et_namaDriver)
        etusiaDriver = findViewById(R.id.et_usiaDriver)
        etnoTelp = findViewById(R.id.et_noTelp)
        edAlamat = findViewById(R.id.et_alamat)
        layoutLoading = findViewById(R.id.layout_loading)

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener{finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle =findViewById<TextView>(R.id.tv_title)
        val id = intent.getLongExtra("id", -1)
        if(id== -1L){
            tvTitle.setText("Tambah Driver")
            btnSave.setOnClickListener { createDriver() }
        }else{
            tvTitle.setText("Edit Driver")
            getDriverById(id)

            btnSave.setOnClickListener { updateDriver(id) }
        }
    }

    private fun getDriverById(id: Long){
        // Fungsi untuk menampilkan data driver berdasarkan id
        setLoading(true)
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, DriverApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val driver = gson.fromJson(response, Driver::class.java)

                etId!!.setText(driver.id)
                etnamaDriver!!.setText(driver.nama_driver)
                etusiaDriver!!.setText(driver.usia_driver)
                etnoTelp!!.setText(driver.noTelp)
                edAlamat!!.setText(driver.alamat)

                MotionToast.createToast(this,
                    "Hurray Berhasil 😍",
                    "Data Berhasil Diambil",
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                setLoading(false)
            },  Response.ErrorListener { error ->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditDriverActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@AddEditDriverActivity, e.message, Toast.LENGTH_SHORT).show()
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

    private fun createDriver() {
        setLoading(true)

        if (etnamaDriver!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditDriverActivity, "Nama Driver tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else if (etusiaDriver!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditDriverActivity, "Usia Driver tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else if (etnoTelp!!.text.toString().isEmpty()) {
            Toast.makeText(
                this@AddEditDriverActivity,
                "No telepon tidak boleh kosong!",
                Toast.LENGTH_SHORT
            ).show()
        } else if (edAlamat!!.toString().isEmpty()) {
            Toast.makeText(this@AddEditDriverActivity, "Alamat tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else {

            val driver = Driver(
                etId!!.text.toString(),
                etnamaDriver!!.text.toString(),
                etusiaDriver!!.text.toString(),
                etnoTelp!!.text.toString(),
                edAlamat!!.text.toString()
            )

            val stringRequest: StringRequest =
                object :
                    StringRequest(Method.POST, DriverApi.ADD_URL, Response.Listener { response ->
                        val gson = Gson()
                        val driver = gson.fromJson(response, Driver::class.java)

                        if (driver != null)
                            MotionToast.createToast(
                                this,
                                "Hurray success 😍",
                                "Data Berhasil Ditambahkan!",
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(
                                    this,
                                    www.sanju.motiontoast.R.font.helvetica_regular
                                )
                            )

                        val returnIntent = Intent()
                        setResult(RESULT_OK, returnIntent)
                        finish()

                        setLoading(false)
                    }, Response.ErrorListener { error ->
                        setLoading(false)
                        try {
                            val responseBody =
                                String(error.networkResponse.data, StandardCharsets.UTF_8)
                            val errors = JSONObject(responseBody)
                            Toast.makeText(
                                this@AddEditDriverActivity,
                                errors.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@AddEditDriverActivity, e.message, Toast.LENGTH_SHORT)
                                .show()
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
                        val requestBody = gson.toJson(driver)
                        return requestBody.toByteArray(StandardCharsets.UTF_8)
                    }

                    override fun getBodyContentType(): String {
                        return "application/json"
                    }
                }
            // Menambahkan request ke request queue
            queue!!.add(stringRequest)
        }

        setLoading(false)
    }

    private fun updateDriver(id: Long){
        setLoading(true)

        val driver = Driver(
            etId!!.text.toString(),
            etnamaDriver!!.text.toString(),
            etusiaDriver!!.text.toString(),
            etnoTelp!!.text.toString(),
            edAlamat!!.text.toString()
        )

        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, DriverApi.UPDATE_URL + id, Response.Listener{ response ->
                val gson = Gson()

                val driver = gson.fromJson(response, Driver::class.java)

                if(driver != null)
                    MotionToast.createToast(this,
                        "Hurray success 😍",
                        "Data Berhasil DiUpdate!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener{ error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditDriverActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@AddEditDriverActivity, e.message, Toast.LENGTH_SHORT).show()
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
                val requestBody = gson.toJson(driver)
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