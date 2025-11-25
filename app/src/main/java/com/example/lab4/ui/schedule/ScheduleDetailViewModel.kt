package com.example.lab4.ui.schedule

import android.app.Application
import androidx.lifecycle.*
import com.example.lab4.model.ScheduleResponse
import com.example.lab4.repository.ScheduleRepository
import kotlinx.coroutines.launch

class ScheduleDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ScheduleRepository(application)

    private val _schedule = MutableLiveData<ScheduleResponse>()
    val schedule: LiveData<ScheduleResponse> = _schedule

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess

    fun loadSchedule(id: Long) {
        viewModelScope.launch {
            try {
                val response = repository.getScheduleById(id)
                if (response.isSuccessful) {
                    _schedule.value = response.body()
                } else {
                    _error.value = "Failed to load schedule: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> = _deleteSuccess

    fun deleteSchedule(id: Long) {
        viewModelScope.launch {
            try {
                val response = repository.deleteSchedule(id)
                if (response.isSuccessful) {
                    _deleteSuccess.value = true
                } else {
                    _error.value = "Failed to delete: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateScheduleDetails(id: Long, notes: String, duration: Int) {
        viewModelScope.launch {
            try {
                val response = repository.updateSchedule(id, notes, duration)
                if (response.isSuccessful) {
                    _schedule.value = response.body() // Frissítjük a nézetet az új adatokkal
                    _updateSuccess.value = true
                } else {
                    _error.value = "Update failed: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}