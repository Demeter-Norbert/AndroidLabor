package com.example.lab4.ui.profile

import android.app.Application
import androidx.lifecycle.*
import com.example.lab4.model.ProfileResponse
import com.example.lab4.repository.ProfileRepository
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ProfileRepository(application)

    private val _profile = MutableLiveData<ProfileResponse?>()
    val profile: LiveData<ProfileResponse?> = _profile

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.getProfile()
                if (response.isSuccessful) {
                    _profile.value = response.body()
                } else {
                    _message.value = "Failed to load profile: ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun uploadProfileImage(file: File) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.uploadImage(file)
                if (response.isSuccessful) {
                    _profile.value = response.body()
                    _message.value = "Image uploaded successfully!"
                } else {
                    _message.value = "Upload failed: ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateProfileDetails(username: String, description: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.updateProfile(username, description)
                if (response.isSuccessful) {
                    _profile.value = response.body()
                    _message.value = "Profile updated successfully!"
                } else {
                    _message.value = "Update failed: ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}