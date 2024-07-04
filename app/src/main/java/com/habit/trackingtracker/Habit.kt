package com.habit.trackingtracker
data class Habit(
    val name: String,
    var isCompleted: Boolean = false,
    var isDeleted: Boolean = false,
    var completionCount: Int = 0
)