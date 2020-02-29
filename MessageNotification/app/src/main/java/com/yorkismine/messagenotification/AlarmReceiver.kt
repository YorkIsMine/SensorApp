package com.yorkismine.messagenotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver() : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {

        Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel("Channel_id_main", "Name", NotificationManager.IMPORTANCE_DEFAULT)

            val manager =
                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context!!, "Channel_id_main")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentText("Text")
            .setContentTitle("Title")
            .setAutoCancel(true)

        val notificationManagerCompat = NotificationManagerCompat.from(context)

        notificationManagerCompat.notify(10000, builder.build())

    }

}