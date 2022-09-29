package com.example.loginactivity.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.loginactivity.EditProfileActivity
import com.example.loginactivity.databinding.FragmentProfileBinding
import com.example.loginactivity.room.UserDB
import com.example.loginactivity.room.UserDao

class Profile_Fragment : Fragment() {
    private lateinit var dao: UserDao
    var sharedPreferences: SharedPreferences? = null
    var itemBinding: FragmentProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(layoutInflater)
        dao = UserDB.getInstance(requireContext()).userDao()

        sharedPreferences = this.getActivity()?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("id", "")
        itemBinding?.tvname?.text = dao.getUser(id!!.toInt()).username
        itemBinding?.tvemail?.text = dao.getUser(id!!.toInt()).email
        itemBinding?.tvphonenumber?.text = dao.getUser(id!!.toInt()).phonenumber
        itemBinding?.tvbirthdate?.text = dao.getUser(id!!.toInt()).birthdate

        binding.btnEdit.setOnClickListener{
            val intent = Intent(this@Profile_Fragment.requireContext(),EditProfileActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}