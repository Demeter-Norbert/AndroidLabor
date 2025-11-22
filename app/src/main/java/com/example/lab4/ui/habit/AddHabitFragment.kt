package com.example.lab4.ui.habit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lab4.R
import com.example.lab4.model.HabitCategory

class AddHabitFragment : Fragment() {

    private val viewModel: AddHabitViewModel by viewModels()
    private lateinit var spinner: Spinner
    private var categoriesList: List<HabitCategory> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_habit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etName = view.findViewById<EditText>(R.id.etName)
        val etDesc = view.findViewById<EditText>(R.id.etDescription)
        val etGoal = view.findViewById<EditText>(R.id.etGoal)
        spinner = view.findViewById(R.id.spinnerCategory)
        val btnSave = view.findViewById<Button>(R.id.btnSave)

        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoriesList = categories
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categories.map { it.name }
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }


        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val desc = etDesc.text.toString()
            val goal = etGoal.text.toString()

            if (categoriesList.isNotEmpty()) {
                val selectedPosition = spinner.selectedItemPosition
                val selectedCategory = categoriesList[selectedPosition]

                if (name.isNotEmpty() && goal.isNotEmpty()) {
                    viewModel.createHabit(name, desc, selectedCategory.id, goal)
                } else {
                    Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.saveSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Habit created successfully!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }
}