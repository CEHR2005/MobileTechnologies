package com.example.mobiletechnologies.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobiletechnologies.R
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiletechnologies.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val tasks = mutableListOf<Task>()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        taskAdapter = TaskAdapter(tasks) { task ->
            showTaskDialog(task)
        }
        binding.recyclerViewTasks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
        }

        binding.fabAddTask.setOnClickListener {
            showTaskDialog(null)
        }

        return root
    }

    private fun showTaskDialog(task: Task?) {
        val dialog = TaskDialogFragment(task) { updatedTask ->
            if (task == null) {
                updatedTask.id = tasks.size + 1 // Incremental ID
                taskAdapter.addTask(updatedTask)
            } else {
                taskAdapter.updateTask(updatedTask)
            }
        }
        dialog.show(childFragmentManager, "TaskDialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
