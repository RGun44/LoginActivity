package com.example.loginactivity.Volley

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
import kotlinx.android.synthetic.main.activity_add_edit.*
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class AddEditActivity : AppCompatActivity() {
    companion object{
        private val KECEPTAN_LIST = arrayOf(
            "Hemat",
            "Reguler",
            "Same Day",
            "Instant",
        )
    }

    private var etId: EditText? = null
    private var etDaerahAsal: EditText? = null
    private var etDaerahTujuan: EditText? = null
    private var etBeratPaket: EditText? = null
    private var edKecepatan: AutoCompleteTextView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        //Pendeklarasian request queue
        queue = Volley.newRequestQueue(this)
        etId = findViewById(R.id.et_id)
        etDaerahAsal = findViewById(R.id.et_daerahAsal)
        etDaerahTujuan = findViewById(R.id.et_daerahTujuan)
        etBeratPaket = findViewById(R.id.et_beratPaket)
        edKecepatan = findViewById(R.id.ed_kecepatan)
        layoutLoading = findViewById(R.id.layout_loading)

        setExposedDropDownMenu()

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener{finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle =findViewById<TextView>(R.id.tv_title)
        val id = intent.getLongExtra("id", -1)
        if(id== -1L){
            tvTitle.setText("Tambah Paket")
            btnSave.setOnClickListener { createPaket() }
        }else{
            tvTitle.setText("Edit Paket")
            getPaketById(id)

            btnSave.setOnClickListener { updatePaket(id) }
        }

    }

    fun setExposedDropDownMenu(){
        val adapterKecepatan: ArrayAdapter<String> =ArrayAdapter<String>(this,
            R.layout.item_list, KECEPTAN_LIST)
        edKecepatan!!.setAdapter(adapterKecepatan)

    }

    private fun getPaketById(id: Long){
        // Fungsi untuk menampilkan data paket berdasarkan id
        setLoading(true)
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, PaketApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val paket = gson.fromJson(response, Paket::class.java)

                etId!!.setText(paket.id)
                etDaerahAsal!!.setText(paket.daerah_asal)
                etDaerahTujuan!!.setText(paket.daerah_tujuan)
                etBeratPaket!!.setText(paket.berat_paket)
                edKecepatan!!.setText(paket.kecepatan)
                setExposedDropDownMenu()

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
                        this@AddEditActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@AddEditActivity, e.message, Toast.LENGTH_SHORT).show()
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

    private fun createPaket() {
        setLoading(true)

        if (etDaerahAsal!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditActivity, "Daerah Asal tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else if (etDaerahTujuan!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditActivity, "Daerah Tujuan tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else if (etBeratPaket!!.text.toString().isEmpty()) {
            Toast.makeText(
                this@AddEditActivity,
                "Berat Paket tidak boleh kosong!",
                Toast.LENGTH_SHORT
            ).show()
        } else if (ed_kecepatan!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditActivity, "Kecepatan tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else {

            val paket = Paket(
                etId!!.text.toString(),
                etDaerahAsal!!.text.toString(),
                etDaerahTujuan!!.text.toString(),
                etBeratPaket!!.text.toString(),
                edKecepatan!!.text.toString()

            )
            val stringRequest: StringRequest =
                object :
                    StringRequest(Method.POST, PaketApi.ADD_URL, Response.Listener { response ->
                        val gson = Gson()
                        val paket = gson.fromJson(response, Paket::class.java)

                        if (paket != null)
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
                                this@AddEditActivity,
                                errors.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@AddEditActivity, e.message, Toast.LENGTH_SHORT)
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
                        val requestBody = gson.toJson(paket)
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


    private fun updatePaket(id: Long){
        setLoading(true)

        val paket = Paket(
            etId!!.text.toString(),
            etDaerahAsal!!.text.toString(),
            etDaerahTujuan!!.text.toString(),
            etBeratPaket!!.text.toString(),
            edKecepatan!!.text.toString()
        )

        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, PaketApi.UPDATE_URL + id, Response.Listener{ response ->
                val gson = Gson()

                val paket = gson.fromJson(response, Paket::class.java)

                if(paket != null)
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
                        this@AddEditActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@AddEditActivity, e.message, Toast.LENGTH_SHORT).show()
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
                val requestBody = gson.toJson(paket)
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