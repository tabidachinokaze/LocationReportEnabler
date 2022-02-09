package cn.tabidachinokaze.locationreportenabler.ui.settings

import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import cn.tabidachinokaze.locationreportenabler.R
import cn.tabidachinokaze.locationreportenabler.util.SimOperatorUtil
import cn.tabidachinokaze.locationreportenabler.util.Util
import cn.tabidachinokaze.locationreportenabler.view.OperatorListPreference
import cn.tabidachinokaze.locationreportenabler.view.OperatorTableLayout

class SettingsFragment : PreferenceFragmentCompat() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(SettingsViewModel::class.java)
    }
    private lateinit var countryPreference: OperatorListPreference
    private lateinit var operatorPreference: OperatorListPreference
    private lateinit var enableSwitchPref: SwitchPreferenceCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_settings, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_switch -> {
                Toast.makeText(
                    requireContext(),
                    requireContext().getString(R.string.apply),
                    Toast.LENGTH_SHORT
                ).show()
                apply()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val telephonyManager =
            requireContext().getSystemService(TelephonyManager::class.java) as TelephonyManager
        val simOperatorUtil = SimOperatorUtil(requireContext())
        countryPreference = findPreference(Util.COUNTRY)!!
        operatorPreference = findPreference(Util.OPERATOR)!!
        enableSwitchPref = findPreference(Util.SWITCH_ENABLE_REPORT)!!

        countryPreference.apply {
            value = viewModel.country.ifBlank {
                telephonyManager.simCountryIso.uppercase()
            }
            setEntriesAndValues(simOperatorUtil.getCountries())
            setOnPreferenceChangeListener { _, newValue ->
                operatorPreference.apply {
                    setEntriesAndValues(simOperatorUtil.getOperators(newValue.toString()))
                    setValueIndex(0)
                }
                true
            }
        }

        operatorPreference.apply {
            value = viewModel.numeric.ifBlank {
                telephonyManager.simOperator
            }
            setEntriesAndValues(simOperatorUtil.getOperators(countryPreference.value ?: ""))
        }

        val infoPreference = findPreference<Preference>(Util.INFO)
        infoPreference?.setOnPreferenceClickListener {
            val dialog = AlertDialog.Builder(requireContext()).apply {
                setCustomTitle(TextView(context).apply {
                    text = context.getString(R.string.info)
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                    layoutParams?.apply {
                        layoutParams = (layoutParams as LinearLayout.LayoutParams).apply {
                            setMargins(0, 100, 0, 100)
                        }
                    }
                    setPadding(0, 10, 0, 10)
                    gravity = Gravity.CENTER
                    setBackgroundColor(context.getColor(R.color.purple_200))
                })
                simOperatorUtil.getSimOperator(operatorPreference.value)?.apply {
                    setView(OperatorTableLayout(context).setInfo(this))
                }
                show()
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.country = countryPreference.value
        viewModel.numeric = operatorPreference.value
        apply()
    }

    private fun apply() {
        if (enableSwitchPref.isChecked) {
            Util.enableLocationReport(operatorPreference.value, countryPreference.value)
        }
    }
}