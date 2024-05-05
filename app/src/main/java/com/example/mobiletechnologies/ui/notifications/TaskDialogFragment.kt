package com.example.mobiletechnologies.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.fragment.app.DialogFragment
import com.example.mobiletechnologies.R

class TaskDialogFragment(
    private val task: Task?,
    private val onSave: (Task) -> Unit
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_dialog, container, false)

        val editTextTitle = view.findViewById<EditText>(R.id.edittext_task_title)
        val switchCompleted = view.findViewById<Switch>(R.id.switch_task_completed)
        val buttonSave = view.findViewById<Button>(R.id.button_save_task)

        task?.let {
            editTextTitle.setText(it.title)
            switchCompleted.isChecked = it.completed
        }

        buttonSave.setOnClickListener {
            val updatedTask = Task(
                task?.id ?: 0,
                editTextTitle.text.toString(),
                switchCompleted.isChecked
            )
            onSave(updatedTask)
            dismiss()
        }

        return view
    }
}
