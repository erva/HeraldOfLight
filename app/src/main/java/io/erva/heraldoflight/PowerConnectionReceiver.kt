package io.erva.heraldoflight

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import io.erva.heraldoflight.MessageWorker.Companion.startWorker
import timber.log.Timber

class PowerConnectionReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
        Timber.d("isCharging: $isCharging")
        context?.let {
            Database(context).status = Pair(isCharging, System.currentTimeMillis())
            startWorker(context)
        }
    }
}
