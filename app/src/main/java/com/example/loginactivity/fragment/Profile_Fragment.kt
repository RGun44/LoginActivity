package com.example.loginactivity.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.loginactivity.EditProfileActivity
import com.example.loginactivity.R
import com.example.loginactivity.databinding.FragmentProfileBinding

class Profile_Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(layoutInflater)
        binding.btnEdit.setOnClickListener{
            val intent = Intent(this@Profile_Fragment.requireContext(),EditProfileActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

}