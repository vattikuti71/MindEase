package com.example.mindease.notifications

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mindease.R

class ReminderWorker(context: Context, params: WorkerParameters): Worker(context, params) {
    private val CHANNEL_ID = "mindease_notifications"

    override fun doWork(): Result { // main work
        // notification title
        val title = inputData.getString("title") ?: "MindEase"
        // notification body
        val body = inputData.getString("body") ?: "Time for your daily check-in"

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .build()

        // show notification
        NotificationManagerCompat.from(applicationContext)
            .notify(System.currentTimeMillis().toInt(), notification)

        return Result.success()
    }
}