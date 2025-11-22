package com.example.lab4.ui.schedule

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lab4.R
import com.example.lab4.model.HabitResponse

class CreateScheduleFragment : Fragment() {

    private val viewModel: CreateScheduleViewModel by viewModels()
    private lateinit var spinner: Spinner
    private var habitList: List<HabitResponse> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_schedule, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinner = view.findViewById(R.id.spinnerHabits)
        val btnNewHabit = view.findViewById<Button>(R.id.btnCreateNewHabit)
        val btnSave = view.findViewById<Button>(R.id.btnSaveSchedule)

        viewModel.loadHabits()

        btnNewHabit.setOnClickListener {
            findNavController().navigate(R.id.action_createScheduleFragment_to_addHabitFragment)
        }

        btnSave.setOnClickListener {
            if (habitList.isNotEmpty()) {
                val selectedHabit = habitList[spinner.selectedItemPosition]
                val hour = view.findViewById<EditText>(R.id.etHour).text.toString().toIntOrNull() ?: 9
                val min = view.findViewById<EditText>(R.id.etMinute).text.toString().toIntOrNull() ?: 0
                val duration = view.findViewById<EditText>(R.id.etDuration).text.toString().toIntOrNull() ?: 30
                val notes = view.findViewById<EditText>(R.id.etNotes).text.toString()

                viewModel.createSchedule(selectedHabit.id, hour, min, duration, notes)
            }
        }

        // Figyelők
        viewModel.habits.observe(viewLifecycleOwner) { habits ->
            habitList = habits
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, habits.map { it.name })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }


        viewModel.operationStatus.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Schedule created!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_createScheduleFragment_to_homeFragment)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_createScheduleFragment_to_homeFragment)
            Log.e("CreateScheduleFragment", "Error: $msg")
        }
    }
}