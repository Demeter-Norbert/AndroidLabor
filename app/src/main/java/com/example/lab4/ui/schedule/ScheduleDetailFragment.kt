package com.example.lab4.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.lab4.R
import java.time.format.DateTimeFormatter
import androidx.navigation.fragment.findNavController
import android.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText

class ScheduleDetailFragment : Fragment() {

    private val viewModel: ScheduleDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scheduleId = arguments?.getLong("scheduleId") ?: -1L

        val btnDelete = view.findViewById<Button>(R.id.btnDeleteSchedule)

        val btnEdit = view.findViewById<Button>(R.id.btnEditSchedule)

        if (scheduleId != -1L) {
            viewModel.loadSchedule(scheduleId)
        } else {
            Toast.makeText(context, "Invalid Schedule ID", Toast.LENGTH_SHORT).show()
        }

        val tvTitle = view.findViewById<TextView>(R.id.tvDetailTitle)
        val tvTime = view.findViewById<TextView>(R.id.tvDetailTime)
        val tvDuration = view.findViewById<TextView>(R.id.tvDetailDuration)
        val tvNotes = view.findViewById<TextView>(R.id.tvDetailNotes)

        viewModel.schedule.observe(viewLifecycleOwner) { schedule ->
            tvTitle.text = schedule.habit?.name ?: "Unknown Habit"

            val start = schedule.startTime?.format(DateTimeFormatter.ofPattern("HH:mm"))
            val end = schedule.endTime?.format(DateTimeFormatter.ofPattern("HH:mm"))
            tvTime.text = "$start - $end"

            tvDuration.text = "${schedule.durationMinutes} minutes"
            tvNotes.text = schedule.notes ?: "No notes provided."
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
        }

        btnDelete.setOnClickListener {
            showDeleteConfirmationDialog(scheduleId)
        }

        btnEdit.setOnClickListener {
            showEditDialog()
        }

        viewModel.updateSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Schedule updated successfully!", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.deleteSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Schedule deleted successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

    }
    private fun showDeleteConfirmationDialog(scheduleId: Long) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Delete Schedule")
            .setMessage("Are you sure you want to delete this schedule?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteSchedule(scheduleId)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditDialog() {
        val currentSchedule = viewModel.schedule.value ?: return

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_schedule, null)
        val etDuration = dialogView.findViewById<TextInputEditText>(R.id.etEditDuration)
        val etNotes = dialogView.findViewById<TextInputEditText>(R.id.etEditNotes)

        etDuration.setText(currentSchedule.durationMinutes?.toString() ?: "")
        etNotes.setText(currentSchedule.notes ?: "")

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newDuration = etDuration.text.toString().toIntOrNull() ?: 0
                val newNotes = etNotes.text.toString()

                val id = currentSchedule.id
                viewModel.updateScheduleDetails(id, newNotes, newDuration)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

