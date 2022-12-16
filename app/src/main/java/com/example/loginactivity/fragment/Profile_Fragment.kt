package com.example.loginactivity.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.loginactivity.EditProfileActivity
import com.example.loginactivity.R
import com.example.loginactivity.Volley.Paket
import com.example.loginactivity.Volley.PaketApi
import com.example.loginactivity.VolleyUser.AddEditProfile
import com.example.loginactivity.VolleyUser.Profile
import com.example.loginactivity.VolleyUser.UserApi
import com.example.loginactivity.databinding.ActivityAddEditProfileBinding
import com.example.loginactivity.databinding.FragmentProfileBinding
import com.example.loginactivity.room.UserDB
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.StandardCharsets


class Profile_Fragment : Fragment() {
    var sharedPreferences: SharedPreferences? = null
    private var queue: RequestQueue? = null

    private var username: TextView? = null
    private var password: TextView? = null
    private var email: TextView? = null
    private var phonenumber: TextView? = null
    private var birthdate: TextView? = null

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queue = Volley.newRequestQueue(requireActivity())
        sharedPreferences = activity?.getSharedPreferences("myPref", Context.MODE_PRIVATE)

        username = binding.tvname
        password = binding.tvpassword
        email = binding.tvemail
        phonenumber = binding.tvphonenumber
        birthdate = binding.tvbirthdate

        val id = sharedPreferences?.getString("id",null)
        setProfile(id!!.toLong())

        btnEdit.setOnClickListener(){
            activity?.let{
                val intent = Intent (it, AddEditProfile::class.java)
                it.startActivity(intent)
            }
        }
    }

    private fun setProfile(id: Long){
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, UserApi.GET_BY_ID_URL + id, Response.Listener { response ->
//                val gson = Gson()
//                val profile = gson.fromJson(response, Profile::class.java)

                var joUser = JSONObject(response.toString())
                val userdata = joUser.getJSONObject("data")

                username!!.setText(userdata.getString("username"))
                password!!.setText(userdata.getString("password"))
                email!!.setText(userdata.getString("email"))
                phonenumber!!.setText(userdata.getString("phonenumber"))
                birthdate!!.setText(userdata.getString("birthdate"))


            },  Response.ErrorListener { error ->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this.requireActivity(),
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this.requireActivity(), e.message, Toast.LENGTH_SHORT).show()
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