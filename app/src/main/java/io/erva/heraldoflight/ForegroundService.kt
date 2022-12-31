package io.erva.heraldoflight

import android.app.*
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat


class ForegroundService : Service() {

    companion object {

        private const val EXTRA_CHARGING = "EXTRA_CHARGING"

        fun startService(context: Context) {
            val serviceIntent = Intent(context, ForegroundService::class.java)
            ContextCompat.startForegroundService(context, serviceIntent)
        }

        fun processChargeState(context: Context, isCharging: Boolean?) {
            if (isCharging == null) return
            val serviceIntent = Intent(context, ForegroundService::class.java)
            serviceIntent.putExtra("EXTRA_CHARGING", isCharging)
            ContextCompat.startForegroundService(context, serviceIntent)
        }
    }

    private val notificationId = 1
    private val channelId = "Herald of Light channel"

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val isCharging = if (intent.hasExtra(EXTRA_CHARGING)) {
            intent.getBooleanExtra(EXTRA_CHARGING, false)
        } else null

        createNotificationChannel()
        val notification = createNotification()
        startForeground(notificationId, notification)

        listenChargerBroadcast()
        handleChargingState(isCharging)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            channelId,
            "Foreground Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        getSystemService(NotificationManager::class.java)?.createNotificationChannel(serviceChannel)
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Herald of Light")
            .setSmallIcon(R.drawable.ic_service)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun listenChargerBroadcast() {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        val receiver = ChargerBroadcastReceiver()
        registerReceiver(receiver, filter)
    }

    private fun handleChargingState(isCharging: Boolean?) {
        if (isCharging == null) return
        val database = Database(applicationContext)
        if (database.isOnPause) return

        database.status = Pair(isCharging, System.currentTimeMillis())
        MessageWorker.startWorker(applicationContext)
    }
}
