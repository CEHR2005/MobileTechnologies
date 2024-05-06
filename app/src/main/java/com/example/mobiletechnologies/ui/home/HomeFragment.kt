package com.example.mobiletechnologies.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mobiletechnologies.DatabaseHelper
import com.example.mobiletechnologies.R

class HomeFragment : Fragment() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var spinnerExerciseType: Spinner
    private lateinit var editTextDuration: EditText
    private lateinit var buttonSave: Button
    private lateinit var textViewTimer: TextView
    private lateinit var buttonStartTimer: Button
    private lateinit var buttonStopTimer: Button
    private lateinit var buttonResetTimer: Button

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private var timeInSeconds = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        dbHelper = DatabaseHelper(requireContext())
        spinnerExerciseType = view.findViewById(R.id.spinner_exercise_type)
        editTextDuration = view.findViewById(R.id.edittext_duration)
        buttonSave = view.findViewById(R.id.button_save)
        textViewTimer = view.findViewById(R.id.textview_timer)
        buttonStartTimer = view.findViewById(R.id.button_start_timer)
        buttonStopTimer = view.findViewById(R.id.button_stop_timer)
        buttonResetTimer = view.findViewById(R.id.button_reset_timer)

        loadExerciseTypes()
        buttonSave.setOnClickListener { saveExercise() }

        buttonStartTimer.setOnClickListener { startTimer() }
        buttonStopTimer.setOnClickListener { stopTimer() }
        buttonResetTimer.setOnClickListener { resetTimer() }

        return view
    }

    private fun loadExerciseTypes() {
        val db = dbHelper.readableDatabase
        val exerciseTypes = mutableListOf<String>()
        val cursor = db.rawQuery("SELECT ${DatabaseHelper.COL_TYPE_NAME} FROM ${DatabaseHelper.TABLE_EXERCISE_TYPE}", null)
        if (cursor.moveToFirst()) {
            do {
                exerciseTypes.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TYPE_NAME)))
            } while (cursor.moveToNext())
        }
        cursor.close()

        // Check if data is loaded
        if (exerciseTypes.isNotEmpty()) {
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, exerciseTypes)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerExerciseType.adapter = adapter
        } else {
            Toast.makeText(requireContext(), "No exercise types available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveExercise() {
        val type = spinnerExerciseType.selectedItem?.toString() ?: return
        val duration = editTextDuration.text.toString().toIntOrNull() ?: return

        val db = dbHelper.writableDatabase
        val query = """
            INSERT INTO ${DatabaseHelper.TABLE_EXERCISE_RECORD}
                (${DatabaseHelper.COL_TYPE_ID}, ${DatabaseHelper.COL_DURATION})
            VALUES ((SELECT ${DatabaseHelper.COL_ID} FROM ${DatabaseHelper.TABLE_EXERCISE_TYPE} WHERE ${DatabaseHelper.COL_TYPE_NAME} = ?), ?)
        """.trimIndent()
        val statement = db.compileStatement(query)
        statement.bindString(1, type)
        statement.bindLong(2, duration.toLong())
        val id = statement.executeInsert()

        if (id != -1L) {
            Toast.makeText(requireContext(), "Exercise saved successfully!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Failed to save exercise.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTimer() {
        runnable = object : Runnable {
            override fun run() {
                timeInSeconds++
                updateTimerDisplay()
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable!!)
    }

    private fun stopTimer() {
        runnable?.let { handler.removeCallbacks(it) }
    }

    private fun resetTimer() {
        stopTimer()
        timeInSeconds = 0
        updateTimerDisplay()
    }

    private fun updateTimerDisplay() {
        val minutes = timeInSeconds / 60
        val seconds = timeInSeconds % 60
        textViewTimer.text = String.format("%02d:%02d", minutes, seconds)
    }
}
