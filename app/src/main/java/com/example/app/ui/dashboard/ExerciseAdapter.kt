package com.example.app.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiletechnologies.R

class ExerciseAdapter(
    private var exercises: List<Exercise>,
    private val onExerciseLongClick: (Exercise) -> Unit // Long click callback
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.nameTextView.text = exercise.name
        holder.durationTextView.text = "${exercise.duration} mins"

        // Add long click listener to handle item deletion
        holder.itemView.setOnLongClickListener {
            onExerciseLongClick(exercise)
            true
        }
    }

    override fun getItemCount(): Int = exercises.size

    // Method to update data when the dataset changes
    fun updateData(newExercises: List<Exercise>) {
        exercises = newExercises
        notifyDataSetChanged()
    }

    class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.text_name)
        val durationTextView: TextView = view.findViewById(R.id.text_duration)
    }
}
