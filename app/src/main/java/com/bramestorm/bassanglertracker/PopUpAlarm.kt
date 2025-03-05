package com.bramestorm.bassanglertracker

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity

class PopUpAlarm : AppCompatActivity() {

    private lateinit var timePicker: TimePicker
    private lateinit var btnSetAlarm: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_alarm)

        timePicker = findViewById(R.id.timePicker)
        btnSetAlarm = findViewById(R.id.btnSetAlarm)
        btnCancel = findViewById(R.id.btnCancel)

        btnSetAlarm.setOnClickListener {
            val hour = if (Build.VERSION.SDK_INT >= 23) timePicker.hour else timePicker.currentHour
            val minute = if (Build.VERSION.SDK_INT >= 23) timePicker.minute else timePicker.currentMinute

            val intent = Intent()
            intent.putExtra("ALARM_HOURS", hour)
            intent.putExtra("ALARM_MINUTES", minute)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        btnCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}
