package com.example.loginactivity.VolleyDonasi

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.loginactivity.R
import com.example.loginactivity.Volley.*
import com.example.loginactivity.VolleyDonasi.AddEditDonasiActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class MainDonasiActivity : AppCompatActivity() {
    private var srDonasi: SwipeRefreshLayout? = null
    private var adapter: DonasiAdapter? = null
    private var svDonasi: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null


    companion object {
        const val LAUNCH_ADD_ACTIVITY = 123
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_donasi)

        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srDonasi = findViewById(R.id.sr_donasi)
        svDonasi = findViewById(R.id.sv_donasi)

        srDonasi?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { allDonasi() })
        svDonasi?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })

        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener {
            val i = Intent(this@MainDonasiActivity, AddEditDonasiActivity::class.java)
            startActivityForResult(i, MainDonasiActivity.LAUNCH_ADD_ACTIVITY)
        }

        val rvProduk = findViewById<RecyclerView>(R.id.rv_donasi)
        adapter = DonasiAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allDonasi()
    }

    private fun allDonasi() {
        srDonasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, DonasiApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonData = jsonObject.getJSONArray("data")
                val donasi : Array<Donasi> = gson.fromJson(jsonData.toString(),Array<Donasi>::class.java)


                adapter!!.setDonasiList(donasi)
                adapter!!.filter.filter(svDonasi!!.query)
                srDonasi!!.isRefreshing = false

                if (!donasi.isEmpty())
                    MotionToast.createToast(this,
                        "Hurray Berhasil 😍",
                        "Data Berhasil Diambil",

                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                else
                    MotionToast.createToast(this,
                        "Failed ☹️",
                        "Data Kosong!",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            }, Response.ErrorListener { error ->
                srDonasi!!.isRefreshing = false
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@MainDonasiActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@MainDonasiActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    fun deleteDonasi(id: String) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, DonasiApi.DELETE_URL + id, Response.Listener { response ->
                setLoading(false)

                val gson = Gson()
                var donasi = gson.fromJson(response, Donasi::class.java)
                if (donasi != null)
                    MotionToast.createToast(this,
                        "Warning ☹️",
                        "Data Berhasil Dihapus!",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                allDonasi()
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@MainDonasiActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@MainDonasiActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            // Menambahkan header pada request
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = java.util.HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers

            }

        }
        queue!!.add(stringRequest)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(resultCode, resultCode, data)
        if (requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allDonasi()
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }

}