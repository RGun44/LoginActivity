package com.example.loginactivity.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginactivity.EditProfileActivity
import com.example.loginactivity.R
import com.example.loginactivity.room.UserDB
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Profile_Fragment : Fragment() {
    val db by lazy { activity?.let { UserDB(it) } }
    var sharedPreferences: SharedPreferences? = null
    private val id = "idKey"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("id", "")
        val username: TextView = view.findViewById(R.id.tvname)
        val email: TextView = view.findViewById(R.id.tvemail)
        val phonenumber: TextView = view.findViewById(R.id.tvphonenumber)
        val birthdate: TextView = view.findViewById(R.id.tvbirthdate)
        val password: TextView = view.findViewById(R.id.tvpassword)
        username.setText(db?.userDao()?.getUser(id!!.toInt())?.username)
        email.setText(db?.userDao()?.getUser(id!!.toInt())?.email)
        phonenumber.setText(db?.userDao()?.getUser(id!!.toInt())?.phonenumber)
        birthdate.setText(db?.userDao()?.getUser(id!!.toInt())?.birthdate)
        password.setText(db?.userDao()?.getUser(id!!.toInt())?.password)

        btnEdit.setOnClickListener(){
            activity?.let{
                val intent = Intent (it, EditProfileActivity::class.java)
                it.startActivity(intent)
            }
        }
    }
}