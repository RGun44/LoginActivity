package com.example.ugd3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd3.entity.Daerah

class DaerahFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daerah, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter : RVDaerahAdapter  = RVDaerahAdapter(Daerah.listOfDaerah)

        val rvDaerah : RecyclerView = view.findViewById(R.id.rv_daerah)

        rvDaerah.layoutManager = layoutManager

        rvDaerah.setHasFixedSize(true)

        rvDaerah.adapter = adapter

    }
}