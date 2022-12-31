package io.erva.heraldoflight

import android.content.Context
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
        val message = if (isCharging) {
            "✅ Світло увімкнено"
        } else {
            "❌ Світло зникло"
        }
        return message
    }
}
