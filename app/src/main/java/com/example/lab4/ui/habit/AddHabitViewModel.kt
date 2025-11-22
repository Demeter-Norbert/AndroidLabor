package com.example.lab4.ui.habit

import android.app.Application
import androidx.lifecycle.*
import com.example.lab4.model.HabitCategory
import com.example.lab4.repository.HabitRepository
import kotlinx.coroutines.launch

class AddHabitViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HabitRepository(application)

    private val _categories = MutableLiveData<List<HabitCategory>>()
    val categories: LiveData<List<HabitCategory>> = _categories

    private val _saveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> = _saveSuccess

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val result = repository.getCategories()
                _categories.value = result
            } catch (e: Exception) {
                _error.value = "Failed to load categories: ${e.message}"
            }
        }
    }

    fun createHabit(name: String, description: String, categoryId: Long, goal: String) {
        viewModelScope.launch {
            try {
                val response = repository.createHabit(name, description, categoryId, goal)
                if (response.isSuccessful) {
                    _saveSuccess.value = true
                } else {
                    _error.value = "Error creating habit: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }
}