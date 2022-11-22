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
    val db by lazy { activity?.let { UserDB(it) } }
    var sharedPreferences: SharedPreferences? = null
    private var queue: RequestQueue? = null

    private var username: TextView? = view?.findViewById(R.id.tvname)
    private var password: TextView? = view?.findViewById(R.id.tvpassword)
    private var email: TextView? = view?.findViewById(R.id.tvemail)
    private var phonenumber: TextView? = view?.findViewById(R.id.tvphonenumber)
    private var birthdate: TextView? = view?.findViewById(R.id.tvbirthdate)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queue = Volley.newRequestQueue(requireActivity())
        sharedPreferences = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)

        val id = sharedPreferences?.getString("id","")
        setProfile(id!!.toLong())

        Toast.makeText(
            requireActivity(),
            id,
            Toast.LENGTH_SHORT
        )

        btnEdit.setOnClickListener(){
            activity?.let{
                val intent = Intent (it, AddEditProfile::class.java).apply {
                    putExtra("Edit",id)
                }
                it.startActivity(intent)
            }
        }
    }

    private fun setProfile(id: Long){
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, UserApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val profile = gson.fromJson(response, Profile::class.java)

                username?.setText(profile.username)
                password?.setText(profile.password)
                email?.setText(profile.email)
                phonenumber?.setText(profile.phonenumber)
                birthdate?.setText(profile.birthdate)

            },  Response.ErrorListener { error ->
                try{

                }catch (e: Exception){

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