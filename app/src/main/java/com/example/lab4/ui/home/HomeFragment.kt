package com.example.lab4.ui.home

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab4.R
import com.example.lab4.databinding.FragmentHomeBinding
import com.example.lab4.repository.ScheduleRepository
import java.time.LocalDate

class HomeViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val repository = ScheduleRepository(context)
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class:${modelClass.name}")
    }
}
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: HomeScheduleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the ViewModel using the custom factory
        val factory = HomeViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupObservers()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupUi() {
        // Setup RecyclerView and adapter
        adapter = HomeScheduleAdapter()
        binding.rvSchedules.layoutManager =
            LinearLayoutManager(requireContext())
        binding.rvSchedules.adapter = adapter
        // Add a divider between list items

        binding.rvSchedules.addItemDecoration(DividerItemDecoration(requireContext
            (), LinearLayoutManager.VERTICAL))
        // Fetch today's schedules (format YYYY-MM-DD)
        val today = try { LocalDate.now().toString() } catch (_:
                                                              Exception) { "2025-10-26" }
        viewModel.getScheduleByDay(today)
    }
    private fun setupObservers() {
        viewModel.schedules.observe(viewLifecycleOwner) { schedules ->
            if (!schedules.isNullOrEmpty()) {
                adapter.submitList(schedules)
                binding.tvEmpty.visibility = View.GONE
            } else {
                adapter.submitList(emptyList())
                binding.tvEmpty.visibility = View.VISIBLE
            }
        }
        //viewModel.isLoading.observe



            viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                if (!errorMessage.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    viewModel.clearError()
                }
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}