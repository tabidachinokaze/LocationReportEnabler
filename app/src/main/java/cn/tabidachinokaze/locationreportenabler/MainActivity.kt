package cn.tabidachinokaze.locationreportenabler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.tabidachinokaze.locationreportenabler.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val container = supportFragmentManager.findFragmentById(R.id.container)
        if (container == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, SettingsFragment())
                .commit()
        }
    }
}