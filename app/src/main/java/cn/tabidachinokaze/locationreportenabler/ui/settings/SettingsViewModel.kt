package cn.tabidachinokaze.locationreportenabler.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    var country = ""
    var numeric = ""
    init {
        Log.i("SettingsViewModel", "create")
    }
}