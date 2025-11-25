package com.example.lab4.ui.home

import androidx.lifecycle.*
import android.util.Log
import com.example.lab4.repository.ScheduleRepository
import com.example.lab4.model.ScheduleResponse
import kotlinx.coroutines.launch
/**
 * ViewModel that manages schedule-related data for the ScheduleFragment.
 */
class HomeViewModel(private val repository: ScheduleRepository) :
    ViewModel() {
    private val _schedules = MutableLiveData<List<ScheduleResponse>>()
    val schedules: LiveData<List<ScheduleResponse>> get() = _schedules
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage
    private val _deleteStatus = MutableLiveData<Boolean>()
    val deleteStatus: LiveData<Boolean> = _deleteStatus
    fun getScheduleByDay(day: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getScheduleByDay(day)
                _schedules.value = response
                _errorMessage.value = null
                Log.d("HomeViewModel", "Response: $response")
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load schedules"
                Log.e("HomeViewModel", "Error: ${e.message}", e)
            } finally {
                _isLoading.value = false
                Log.d("HomeViewModel", "Loading: ${_isLoading.value}")
            }
        }
    }

    fun deleteSchedule(id: Long) {
        viewModelScope.launch {
            try {
                val response = repository.deleteSchedule(id)
                if (response.isSuccessful) {
                    _deleteStatus.value = true
                    val today = java.time.LocalDate.now().toString()
                    getScheduleByDay(today)
                } else {
                    _errorMessage.value = "Failed to delete: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}