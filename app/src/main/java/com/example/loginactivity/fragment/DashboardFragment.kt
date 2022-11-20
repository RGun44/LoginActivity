package com.example.loginactivity.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.loginactivity.Volley.MainActivity
import com.example.loginactivity.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDashboardBinding.inflate(layoutInflater)
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this@DashboardFragment.requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}