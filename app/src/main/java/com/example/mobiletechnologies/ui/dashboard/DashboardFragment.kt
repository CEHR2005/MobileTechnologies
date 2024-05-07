package com.example.mobiletechnologies.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.app.ui.dashboard.ExerciseAdapter
import com.example.mobiletechnologies.DatabaseHelper
import com.example.mobiletechnologies.R

class DashboardFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseAdapter

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
        val databaseHelper = DatabaseHelper(requireContext())
        adapter = ExerciseAdapter(databaseHelper.getExercises()) { exercise ->
            if (databaseHelper.deleteExerciseById(exercise.id)) {
                adapter.updateData(databaseHelper.getExercises())
                Toast.makeText(requireContext(), "Exercise deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed to delete exercise", Toast.LENGTH_SHORT).show()
            }
        }
        recyclerView.adapter = adapter
    }
}
