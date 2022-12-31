package io.erva.heraldoflight

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BOOT_COMPLETED

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action == ACTION_BOOT_COMPLETED) {
            context?.let { ForegroundService.startService(it) }
        }
    }
}
