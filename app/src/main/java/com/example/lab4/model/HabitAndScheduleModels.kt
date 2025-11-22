package com.example.lab4.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ScheduleResponse(
    val id: Long,
    @SerializedName("start_time")
    val startTime: LocalDateTime? = null,
    @SerializedName("end_time")
    val endTime: LocalDateTime? = null,
    val status: String? = null,
    val date: String? = null,
    @SerializedName("is_custom")
    val isCustom: Boolean,
    @SerializedName("created_at")
    val createdAt: LocalDateTime? = null,
    @SerializedName("updated_at")
    val updatedAt: LocalDateTime? = null,
    val type: String? = null,
    @SerializedName("duration_minutes")
    val durationMinutes: Int? = null,
    val notes: String? = null,
    val participants: List<ParticipantDto>? = emptyList(),
    val habit: HabitResponse? = null,
    val progress: List<ProgressResponseDto>? = emptyList(),
    val isParticipantOnly: Boolean,
)

data class HabitResponse(
    val id: Long,
    val name: String,
    val description: String? = null,
    val category: HabitCategory,
    val goal: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
data class HabitCategory(
    val id: Long,
    val name: String,
    val iconUrl: String? = null
)
data class ProgressResponseDto(
    val id: Long,
    val userId: Long,
    val progressValue: Double,
    val updatedAt: LocalDateTime
)
data class ParticipantDto(
    val id: Long,
    val name: String,
    val email: String,
    val profileImage: String? = null
)

data class CreateHabitRequest(
    val name: String,
    val description: String,
    val categoryId: Long,
    val goal: String
)

data class CreateScheduleRequest(
    val habitId: Long,
    val start_time: String,
    val end_time: String,
    val duration_minutes: Int,
    val repeatPattern: String = "daily",
    val repeatDays: Int = 30,
    val is_custom: Boolean = true,
    val participantIds: List<Long> = emptyList(),
    val notes: String
)