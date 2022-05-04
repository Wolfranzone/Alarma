package com.example.alarma

import android.app.*
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.EventLogTags
import android.view.inputmethod.InputBinding
import android.widget.Toast
import androidx.core.app.ActivityManagerCompat
import androidx.core.app.ActivityOptionsCompat
import com.example.alarma.databinding.ActivityMainBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*


class MainActivity : AppCompatActivity() {

private lateinit var binding: ActivityMainBinding
private lateinit var picker: MaterialTimePicker
private lateinit var  calendar: Calendar
private lateinit var alarmManager: AlarmManager
private lateinit var pendingIntent: PendingIntent
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CreationNotificationChannel()

        binding.seleccionarhora.setOnClickListener{

            showTimePicker()
        }



        binding.colocarhora.setOnClickListener{

            setAlarma()

        }
        binding.cancelaralarma.setOnClickListener{
cancelAlarm()

        }
    }

    private fun cancelAlarm() {

        alarmManager=getSystemService(ALARM_SERVICE)as AlarmManager

        val intent=Intent(this,Alarmarecibi::class.java)
        pendingIntent= PendingIntent.getBroadcast(this,0,intent,0)
        alarmManager.cancel(pendingIntent)
        Toast.makeText(this,"Alarma cancelada",Toast.LENGTH_LONG).show()
    }

    private fun setAlarma() {
        alarmManager=getSystemService(ALARM_SERVICE)as AlarmManager

        val intent=Intent(this,Alarmarecibi::class.java)
    pendingIntent= PendingIntent.getBroadcast(this,0,intent,0)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,pendingIntent
        )
        Toast.makeText(this,"Alarma establecida en forma segura",Toast.LENGTH_SHORT).show()
    }

    private fun showTimePicker() {

     picker=MaterialTimePicker.Builder()
         .setTimeFormat(TimeFormat.CLOCK_12H)
         .setHour(12)
         .setMinute(0)
         .setTitleText("Seleccionar hora de alarma")
         .build()
        picker .show(supportFragmentManager,"foxandroid")
        picker.addOnPositiveButtonClickListener{
            if(picker.hour>12) {
                binding.seleccionarhora.text =
                    String.format("%02d", picker.hour - 12) + ":" + String.format(
                        "%02d", picker.minute
                    ) + "PM"
            }else{
                String.format("%02d", picker.hour ) + ":" + String.format(
                    "%02d", picker.minute
                ) + "AM"
            }
            calendar= Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY]=picker.hour
            calendar[Calendar.MINUTE]=picker.minute
            calendar[Calendar.SECOND]=0
            calendar[Calendar.MILLISECOND]=0

        }

    }


    private fun CreationNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val name: CharSequence="foxandroidReminderChannel"
            val description="Chanel for Alarm Manager"
            val importance=NotificationManager.IMPORTANCE_HIGH
            val channel=NotificationChannel("foxandroid",name,importance)
            channel.description=description
            val notificationManager=getSystemService(NotificationManager::class.java)


  notificationManager.createNotificationChannel(channel)
        }
    }
}