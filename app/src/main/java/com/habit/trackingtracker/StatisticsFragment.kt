package com.habit.trackingtracker
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
class StatisticsFragment : Fragment() {

    private lateinit var habits: List<Habit>
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    private lateinit var focusTimerFragment: FocusTimerFragment // Declare the instance variable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)

        val totalHabitsTextView: TextView = view.findViewById(R.id.total_habits_text_view)
        val completedHabitsTextView: TextView = view.findViewById(R.id.completed_habits_text_view)
        val focusSessionsTextView: TextView = view.findViewById(R.id.focus_sessions_text_view)

        // Retrieve habits data from shared preferences
        habits = loadHabitsFromSharedPreferences()

        // Initialize FocusTimerFragment instance
        focusTimerFragment = FocusTimerFragment()

        // Simulate data retrieval from FocusTimerFragment
        val sessionsCompleted = focusTimerFragment.getSessionsCompleted()

        val totalHabits = habits.size
        val completedHabits = habits.count { it.isCompleted }

        totalHabitsTextView.text = "Total Habits: $totalHabits"
        completedHabitsTextView.text = "Completed Habits: $completedHabits"
        focusSessionsTextView.text = "Focus Sessions: $sessionsCompleted"

        return view
    }

    private fun loadHabitsFromSharedPreferences(): List<Habit> {
        sharedPreferences = requireActivity().getSharedPreferences(
            "HabitSharedPreferences", Context.MODE_PRIVATE
        )
        val habitsJson = sharedPreferences.getString("habits", null)
        return if (!habitsJson.isNullOrBlank()) {
            val type = object : TypeToken<List<Habit>>() {}.type
            gson.fromJson(habitsJson, type)
        } else {
            emptyList()
        }
    }
}
