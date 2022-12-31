package io.erva.heraldoflight

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ChargerBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        intent.action?.let { action ->
            val isCharging = when (action) {
                Intent.ACTION_POWER_CONNECTED -> true
                Intent.ACTION_POWER_DISCONNECTED -> false
                else -> null
            }
            context?.let {
                ForegroundService.processChargeState(it, isCharging)
            }
        }
    }
}
