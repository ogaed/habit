package com.habit.trackingtracker

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment


import java.util.concurrent.TimeUnit

class FocusTimerFragment : Fragment() {

    private lateinit var timer: CountDownTimer
    private var isRunning = false
    private var timeLeftInMillis: Long = 1500000 // 25 minutes
    private var sessionsCompleted = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_focus_timer, container, false)

        val timerTextView: TextView = view.findViewById(R.id.timer_text_view)
        val startButton: Button = view.findViewById(R.id.start_button)
        val resetButton: Button = view.findViewById(R.id.reset_button)
        val addTimeButton: Button = view.findViewById(R.id.add_button) // New button

        startButton.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        resetButton.setOnClickListener {
            resetTimer()
        }

        addTimeButton.setOnClickListener {
            addExtraTime()
        }

        updateCountDownText(timerTextView)

        return view
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownText(view?.findViewById(R.id.timer_text_view))
            }

            override fun onFinish() {
                isRunning = false
                sessionsCompleted++
                updateButtons()
            }
        }.start()

        isRunning = true
        updateButtons()
    }

    private fun pauseTimer() {
        timer.cancel()
        isRunning = false
        updateButtons()
    }

    private fun resetTimer() {
        timeLeftInMillis = 1500000 // 25 minutes
        updateCountDownText(view?.findViewById(R.id.timer_text_view))
        updateButtons()
    }

    private fun addExtraTime() {
        // Example: Add 5 minutes (300,000 milliseconds) to the timer
        timeLeftInMillis += 300000
        updateCountDownText(view?.findViewById(R.id.timer_text_view))
        updateButtons()
    }

    private fun updateCountDownText(timerTextView: TextView?) {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) % 60
        val timeFormatted = String.format("%02d:%02d", minutes, seconds)
        timerTextView?.text = timeFormatted
    }

    private fun updateButtons() {
        val startButton: Button? = view?.findViewById(R.id.start_button)
        val resetButton: Button? = view?.findViewById(R.id.reset_button)

        if (isRunning) {
            startButton?.text = "Pause"
            resetButton?.visibility = View.INVISIBLE
        } else {
            startButton?.text = "Start"
            if (timeLeftInMillis < 1500000) {
                resetButton?.visibility = View.VISIBLE
            } else {
                resetButton?.visibility = View.INVISIBLE
            }
        }
    }

    // Method to get the number of completed sessions
    fun getSessionsCompleted(): Int {
        return sessionsCompleted
    }
}
