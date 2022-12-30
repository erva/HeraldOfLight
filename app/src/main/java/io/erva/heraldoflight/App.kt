package io.erva.heraldoflight

import android.annotation.SuppressLint
import android.app.Application
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        timber()
    }

    private fun timber() {
        Timber.plant(
            object : Timber.DebugTree() {
                @SuppressLint("DefaultLocale")
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "@@ " +
                            "(${element.fileName}:${element.lineNumber})" +
                            "#${element.methodName} " +
                            "thread[${Thread.currentThread().name}]"
                }
            }
        )
    }
}
