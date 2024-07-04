package com.habit.trackingtracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HabitAdapter(
    private val habits: MutableList<Habit>,
    private val onHabitCompleted: (Int) -> Unit,
    private val onHabitDeleted: (Int) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val currentHabit = habits[position]
        holder.habitNameTextView.text = currentHabit.name
        holder.habitCheckBox.isChecked = currentHabit.isCompleted

        holder.habitCheckBox.setOnCheckedChangeListener(null) // Clear listener to prevent unwanted triggering
        holder.habitCheckBox.isChecked = currentHabit.isCompleted
        holder.habitCheckBox.setOnCheckedChangeListener { _, isChecked ->
            currentHabit.isCompleted = isChecked
            if (isChecked) {
                currentHabit.completionCount++
            }
            onHabitCompleted(position)
        }

        holder.deleteIcon.setOnClickListener {
            onHabitDeleted(position)
        }
    }

    override fun getItemCount() = habits.size

    class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val habitNameTextView: TextView = itemView.findViewById(R.id.habit_name_text_view)
        val habitCheckBox: CheckBox = itemView.findViewById(R.id.habit_check_box)
        val deleteIcon: ImageView = itemView.findViewById(R.id.delete_icon)
    }
}
