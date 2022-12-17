package com.example.loginactivity.VolleyDonasi

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
import kotlinx.android.synthetic.main.activity_add_edit_donasi.*
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class AddEditDonasiActivity : AppCompatActivity() {
    private var etId: EditText? = null
    private var etnamaDonatur: EditText? = null
    private var etjenisDonasi: EditText? = null
    private var etnominalDonasi: EditText? = null
    private var etPembayaran: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_donasi)

        //Pendeklarasian request queue
        queue = Volley.newRequestQueue(this)
        etId = findViewById(R.id.et_id)
        etnamaDonatur = findViewById(R.id.et_namaDonatur)
        etjenisDonasi = findViewById(R.id.et_jenisDonasi)
        etnominalDonasi = findViewById(R.id.et_nominalDonasi)
        etPembayaran = findViewById(R.id.et_Pembayaran)
        layoutLoading = findViewById(R.id.layout_loading)


        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener{finish() }
        val btnSave = findViewById<Button>(R.id.btn_save1)
        val tvTitle =findViewById<TextView>(R.id.tv_titleDonasi)
        val id = intent.getLongExtra("id", -1)
        if(id== -1L){
            tvTitle.setText("Tambah Donasi")
            btnSave.setOnClickListener { createDonasi() }
        }else{
            tvTitle.setText("Edit donasi")
            getDonasiById(id)

            btnSave.setOnClickListener { updateDonasi(id) }
        }

    }

    private fun getDonasiById(id: Long){
        // Fungsi untuk menampilkan data donasi berdasarkan id
        setLoading(true)
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, DonasiApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val donasi = gson.fromJson(response, Donasi::class.java)

                etId!!.setText(donasi.id)
                etnamaDonatur!!.setText(donasi.nama_Donatur)
                etjenisDonasi!!.setText(donasi.jenis_donasi)
                etnominalDonasi!!.setText(donasi.nominal_Donasi)
                etPembayaran!!.setText(donasi.pembayaran)

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
                        this@AddEditDonasiActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@AddEditDonasiActivity, e.message, Toast.LENGTH_SHORT).show()
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

    private fun createDonasi() {
        setLoading(true)

        if (etnamaDonatur!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditDonasiActivity, "Nama Donatur tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else if (etjenisDonasi!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditDonasiActivity, "Jenis Donasitidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else if (etnominalDonasi!!.text.toString().isEmpty()) {
            Toast.makeText(
                this@AddEditDonasiActivity,
                "Nominal donasi tidak boleh kosong!",
                Toast.LENGTH_SHORT
            ).show()
        } else if (etPembayaran!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditDonasiActivity, "Pembayaran tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else {

            val donasi = Donasi(
                etId!!.text.toString(),
                etnamaDonatur!!.text.toString(),
                etjenisDonasi!!.text.toString(),
                etnominalDonasi!!.text.toString(),
                etPembayaran!!.text.toString()

            )
            val stringRequest: StringRequest =
                object :
                    StringRequest(Method.POST, DonasiApi.ADD_URL, Response.Listener { response ->
                        val gson = Gson()
                        val donasi = gson.fromJson(response, Donasi::class.java)

                        if (donasi != null)
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
                                this@AddEditDonasiActivity,
                                errors.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@AddEditDonasiActivity, e.message, Toast.LENGTH_SHORT)
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
                        val requestBody = gson.toJson(donasi)
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


    private fun updateDonasi(id: Long){
        setLoading(true)

        val donasi = Donasi(
            etId!!.text.toString(),
            etnamaDonatur!!.text.toString(),
            etjenisDonasi!!.text.toString(),
            etnominalDonasi!!.text.toString(),
            etPembayaran!!.text.toString()
        )

        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, DonasiApi.UPDATE_URL + id, Response.Listener{ response ->
                val gson = Gson()

                val donasi = gson.fromJson(response, Donasi::class.java)

                if(donasi != null)
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
                        this@AddEditDonasiActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@AddEditDonasiActivity, e.message, Toast.LENGTH_SHORT).show()
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
                val requestBody = gson.toJson(donasi)
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