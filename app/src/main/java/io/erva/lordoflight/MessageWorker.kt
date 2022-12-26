package io.erva.lordoflight

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.work.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MessageWorker(private val context: Context, params: WorkerParameters) :
    Worker(context, params) {

    companion object {
        fun startWorker(context: Context) {
            Timber.d("")
            val name = "MessageWorker"
            val work = OneTimeWorkRequest.Builder(MessageWorker::class.java)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
                .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(name, ExistingWorkPolicy.REPLACE, work)
        }
    }

    override fun doWork(): Result {
        Timber.d("")
        setForegroundAsync(createForegroundInfo())
        val telegramNotifier = TelegramNotifier()
        val database = Database(context)
        val message: String = with(database.status) {
            formatMessage(first, second)
        }
        return if (telegramNotifier.sendMessage(message)) {
            Timber.d("Success")
            Result.success()
        } else {
            Timber.d("Retry")
            Result.retry()
        }
    }

    private fun formatMessage(isCharging: Boolean, time: Long): String {
        return "temp $isCharging $time"
    }

    private fun createForegroundInfo(): ForegroundInfo {
        val id = "id"
        val title = applicationContext.getString(R.string.app_name)
        val channelId = createNotificationChannel("channel id", "Channel")
        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(title)
            .setChannelId(channelId)
            .setTicker(title)
            .setSmallIcon(R.drawable.ic_launcher)
            .setOngoing(true)
            .build()
        return ForegroundInfo(1, notification)
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }
}
