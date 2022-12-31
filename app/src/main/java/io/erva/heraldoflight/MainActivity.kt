package io.erva.heraldoflight

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // TODO add runtime permission for notification (Android 13)

        findViewById<Button>(R.id.start).setOnClickListener {
            Database(applicationContext).isOnPause = true
            ForegroundService.startService(applicationContext)
        }

        findViewById<Button>(R.id.pause).setOnClickListener {
            Database(applicationContext).isOnPause = false
        }
    }
}
