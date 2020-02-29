package com.yorkismine.messagenotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService

class MyService() : Service(){
    private val CHANNEL_ID = "Channel_id_main"
    private lateinit var remoteViews: RemoteViews
    private lateinit var builder: NotificationCompat.Builder
    private val id = 10000

    override fun onBind(intent: Intent?): IBinder? {
        return null;
    }

    override fun onCreate() {
        remoteViews = RemoteViews(packageName, R.layout.custom_notif)
        builder = NotificationCompat.Builder(this@MyService, "Channel_id_main")
        builder.setAutoCancel(true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)

        val seconds = pref.getLong("d", 0)

        Log.d("TESTING", "seconds is $seconds")

        createNotification()

        val timer = object : CountDownTimer((seconds * 1000).toLong(), 1000){
            override fun onFinish() {
                val manager: NotificationManager? = getSystemService()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel =
                        NotificationChannel(CHANNEL_ID, "Name", NotificationManager.IMPORTANCE_DEFAULT)
                    manager!!.createNotificationChannel(channel)
                }

                val i = Intent(this@MyService, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(this@MyService, 0, i, 0)
                builder.setContentIntent(pendingIntent)
                    .setOnlyAlertOnce(false)

                manager!!.notify(id, builder.build())

                onDestroy()
            }

            override fun onTick(millisUntilFinished: Long) {
                remoteViews.setTextViewText(R.id.timer, (millisUntilFinished / 1000).toString())
                builder.setCustomContentView(remoteViews)
                val manager: NotificationManager? = getSystemService()
                manager!!.notify(id, builder.build())
            }

        }

        timer.start()


        return START_STICKY
    }

    override fun onDestroy() {
        stopSelf()
    }


    private fun createNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, "Name", NotificationManager.IMPORTANCE_DEFAULT)

            val manager: NotificationManager? = getSystemService()
            manager!!.createNotificationChannel(channel)
        }

        builder.setSmallIcon(R.drawable.ic_launcher_background)
            .setContentText("Text")
            .setContentTitle("Title")
            .setOnlyAlertOnce(true)
            .setCustomContentView(remoteViews)
            .setOngoing(true)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(id, builder.build())
    }

}