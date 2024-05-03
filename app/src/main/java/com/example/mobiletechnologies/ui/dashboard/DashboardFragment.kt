package com.example.mobiletechnologies.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiletechnologies.R
import com.example.app.ui.dashboard.Exercise
import com.example.app.ui.dashboard.ExerciseAdapter
import com.example.mobiletechnologies.DatabaseHelper

class DashboardFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        recyclerView = view.findViewById(R.id.recycler_view_exercises)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize RecyclerView here
        val databaseHelper = DatabaseHelper(requireContext())
        recyclerView.adapter = ExerciseAdapter(databaseHelper.getExercises())
    }
}