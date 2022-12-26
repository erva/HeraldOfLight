package io.erva.lordoflight

import android.content.Context
import android.content.SharedPreferences

class Database(context: Context) {

    companion object {
        private const val PREFS_FILENAME = "app_prefs"

        private const val KEY_PAUSE = "KEY_PAUSE"
        private const val KEY_STATUS_IS_LIGHT_ON = "KEY_STATUS_IS_LIGHT_ON"
        private const val KEY_STATUS_TIMESTAMP = "KEY_STATUS_TIMESTAMP"
    }

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    /**
     * May be manually paused. Change power socket, etc
     */
    var isOnPause: Boolean
        get() = sharedPrefs.getBoolean(KEY_PAUSE, false)
        set(value) = sharedPrefs.edit().putBoolean(KEY_PAUSE, value).apply()

    /**
     * first - is electricity/charging
     * second - when, unix time
     */
    var status: Pair<Boolean, Long>
        get() {
            return Pair(
                sharedPrefs.getBoolean(KEY_STATUS_IS_LIGHT_ON, false),
                sharedPrefs.getLong(KEY_STATUS_TIMESTAMP, 0L)
            )
        }
        set(value) =
            sharedPrefs.edit()
                .putBoolean(KEY_STATUS_IS_LIGHT_ON, value.first)
                .putLong(KEY_STATUS_TIMESTAMP, value.second).apply()
}
