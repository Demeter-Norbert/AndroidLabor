package com.example.lab4.ui.schedule

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.lab4.model.CreateScheduleRequest
import com.example.lab4.model.HabitResponse
import com.example.lab4.repository.ScheduleRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CreateScheduleViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ScheduleRepository(application)

    private val _habits = MutableLiveData<List<HabitResponse>>()
    val habits: LiveData<List<HabitResponse>> = _habits

    private val _operationStatus = MutableLiveData<Boolean>()
    val operationStatus: LiveData<Boolean> = _operationStatus

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    fun loadHabits() {
        viewModelScope.launch {
            try {
                val result = repository.getHabits()
                _habits.value = result
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load habits: ${e.message}"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createSchedule(habitId: Long, startHour: Int, startMinute: Int, durationMinutes: Int, notes: String) {
        viewModelScope.launch {
            try {

                val now = LocalDateTime.now()
                    .withHour(startHour)
                    .withMinute(startMinute)
                    .withSecond(0)
                    .withNano(0)

                val endTime = now.plusMinutes(durationMinutes.toLong())


                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

                val request = CreateScheduleRequest(
                    habitId = habitId,
                    start_time = now.toString(),
                    end_time = endTime.toString(),
                    duration_minutes = durationMinutes,
                    notes = notes
                )

                val response = repository.createSchedule(request)
                if (response.isSuccessful) {
                    _operationStatus.value = true
                } else {
                    _errorMessage.value = "Error: ${response.code()} ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
}