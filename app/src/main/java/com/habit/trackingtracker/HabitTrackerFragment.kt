package com.habit.trackingtracker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HabitTrackerFragment : Fragment() {

    private lateinit var habitAdapter: HabitAdapter
    private val habits = mutableListOf<Habit>() // Change to MutableList
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(
            "HabitSharedPreferences", Context.MODE_PRIVATE
        )
        loadHabits()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_habit_tracker, container, false)

        val habitRecyclerView: RecyclerView = view.findViewById(R.id.habit_recycler_view)
        habitRecyclerView.layoutManager = LinearLayoutManager(context)
        habitAdapter = HabitAdapter(habits, ::onHabitCompleted, ::onHabitDeleted)
        habitRecyclerView.adapter = habitAdapter

        val addHabitButton: Button = view.findViewById(R.id.add_habit_button)
        val habitEditText: EditText = view.findViewById(R.id.habit_edit_text)

        addHabitButton.setOnClickListener {
            val habitName = habitEditText.text.toString()
            if (habitName.isNotEmpty()) {
                val newHabit = Habit(habitName)
                habits.add(newHabit)
                saveHabits()
                habitAdapter.notifyItemInserted(habits.size - 1)
                habitEditText.text.clear()
            } else {
                Toast.makeText(context, "Please enter a habit name", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun saveHabits() {
        val habitsJson = gson.toJson(habits)
        sharedPreferences.edit().putString("habits", habitsJson).apply()
    }

    private fun loadHabits() {
        val habitsJson = sharedPreferences.getString("habits", null)
        if (!habitsJson.isNullOrBlank()) {
            val type = object : TypeToken<List<Habit>>() {}.type
            habits.clear()
            habits.addAll(gson.fromJson(habitsJson, type))
        }
    }

    private fun onHabitCompleted(position: Int) {
        saveHabits()
    }

    private fun onHabitDeleted(position: Int) {
        habits.removeAt(position)
        saveHabits()
        habitAdapter.notifyItemRemoved(position)
    }
}
