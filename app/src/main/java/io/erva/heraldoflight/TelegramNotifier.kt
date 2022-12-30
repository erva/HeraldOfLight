package io.erva.heraldoflight

import androidx.annotation.WorkerThread
import timber.log.Timber
import java.io.BufferedInputStream
import java.io.IOException
import java.net.URL

class TelegramNotifier {

    companion object {
        private const val urlString = "https://api.telegram.org" +
                "/bot${BuildConfig.API_TOKEN}" +
                "/sendMessage?chat_id=${BuildConfig.CHAT_ID}&text=%s"
    }

    @WorkerThread
    fun sendMessage(message: String): Boolean {
        return try {
            val url = URL(String.format(urlString, message))
            val connection = url.openConnection()
            BufferedInputStream(connection.getInputStream()).use { }
            true
        } catch (e: IOException) {
            Timber.w(e)
            false
        }
    }
}
