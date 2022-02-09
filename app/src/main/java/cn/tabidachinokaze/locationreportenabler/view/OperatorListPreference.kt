package cn.tabidachinokaze.locationreportenabler.view

import android.content.Context
import android.util.AttributeSet
import androidx.preference.ListPreference

class OperatorListPreference(context: Context, attr: AttributeSet) :
    ListPreference(context, attr) {

    override fun getSummary(): CharSequence? {
        return value ?: super.getSummary()
    }

    fun setEntriesAndValues(map: Map<String, String>) {
        entries = map.keys.toTypedArray()
        entryValues = map.values.toTypedArray()
    }
}