package rck.supernacho.ru.rollercalckt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        startActivity(Intent(this, NavActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NO_ANIMATION })
    }
}
