package com.yorkismine.messagenotification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "Channel_id_main"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        usual_notif_btn.setOnClickListener {
            //Done!
            createNotification_A()
        }

        after_n_seconds_notif_btn.setOnClickListener {
            //Done!
            val seconds = writer_et.text.toString().toLong()
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    createNotification_A()
                }

            }, seconds * 1000)
        }


        every_n_seconds_notif_btn.setOnClickListener {
            val seconds = writer_et.text.toString().toLong()

            val intent = Intent(this, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, seconds * 100, pendingIntent)

        }


        undestroyable_notif_btn.setOnClickListener {
            //Done
            createNotification_B()
            val seconds = writer_et.text.toString().toLong()
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val channel =
                            NotificationChannel(
                                CHANNEL_ID,
                                "Name",
                                NotificationManager.IMPORTANCE_DEFAULT
                            )

                        val manager: NotificationManager? = getSystemService()
                        manager!!.createNotificationChannel(channel)
                    }

                    val intent = Intent(this@MainActivity, MainActivity::class.java)

                    val pendingIntent = PendingIntent
                        .getActivity(this@MainActivity, 0, intent, 0)

                    val builder = NotificationCompat.Builder(this@MainActivity, CHANNEL_ID)
                        .setContentTitle("Title")
                        .setContentText("This is notification")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)

                    val managerCompat = NotificationManagerCompat.from(this@MainActivity)
                    managerCompat.notify(10000, builder.build())
                }

            }, seconds * 1000)
        }

        github_notif_btn.setOnClickListener { //Done!
            createNotification_C()
        }


        timer_shower_notif_btn.setOnClickListener{
            val seconds = writer_et.text.toString().toLong()
            val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putLong("d", seconds)
            editor.apply()

            val intent = Intent(this@MainActivity, MyService::class.java)
            intent.putExtra("d", seconds)
            startService(intent)
        }


    }

    private fun rec(seconds: Long) {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                createNotification_A()
                Log.d("TESTING", "AGAIN")
                rec(seconds)
            }

        }, seconds * 1000)
    }

    private fun createNotification_A() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, "Name", NotificationManager.IMPORTANCE_DEFAULT)

            val manager: NotificationManager? = getSystemService()
            manager!!.createNotificationChannel(channel)
        }

        val notificationCompat = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("This is notification")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()


        val managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(10000, notificationCompat)
    }

    private fun createNotification_B() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, "Name", NotificationManager.IMPORTANCE_DEFAULT)

            val manager: NotificationManager? = getSystemService()
            manager!!.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("This is notification")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)

        val managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(10000, builder.build())
    }

    private fun createNotification_C() {
        val pendingIntent = PendingIntent
            .getActivity(
                this, 0,
                Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/")), 0
            )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, "Name", NotificationManager.IMPORTANCE_DEFAULT)

            val manager: NotificationManager? = getSystemService()
            manager!!.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("This is notification")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(10000, builder.build())

    }

    private fun createNotification_D(remoteViews: RemoteViews){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, "Name", NotificationManager.IMPORTANCE_DEFAULT)

            val manager: NotificationManager? = getSystemService()
            manager!!.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("This is notification")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setCustomContentView(remoteViews)
            .setOngoing(true)

        val managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(10000, builder.build())
    }
}