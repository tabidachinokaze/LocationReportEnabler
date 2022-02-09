package cn.tabidachinokaze.locationreportenabler.util

import android.content.Context
import com.google.gson.Gson
import java.io.InputStreamReader

class SimOperatorUtil(context: Context) {
    private val simOperators: ArrayList<SimOperators.SimOperator>
    private val countryNames = mutableSetOf<String>()

    init {
        val simOperatorsIS = context.assets.open("SimOperators.json")
        val inputStreamReader = InputStreamReader(simOperatorsIS)
        simOperators = Gson().fromJson<ArrayList<SimOperators.SimOperator>>(
            inputStreamReader,
            SimOperators::class.java
        )
        for (simOperator in simOperators) {
            if (simOperator.countryCode != null && simOperator.countryName != null) {
                countryNames.add(simOperator.countryName)
            }
        }
    }

    fun getCountries(): Map<String, String> {
        val countries = mutableMapOf<String, String>()
        for (simOperator in simOperators) {
            if (simOperator.countryCode != null && simOperator.countryName != null) {
                countries[simOperator.countryName] = simOperator.countryCode
            }
        }
        return countries
    }

    fun getOperators(countryCode: String): Map<String, String> {
        val operators = mutableMapOf<String, String>()
        for (simOperator in simOperators) {
            if (countryCode == simOperator.countryCode && simOperator.mcc != null && simOperator.mnc != null) {
                val key =
                    "${simOperator.mcc}${simOperator.mnc} ${simOperator.operator ?: simOperator.bands ?: ""}"
                operators[key] = simOperator.mcc + simOperator.mnc
            }
        }
        return operators
    }

    fun getSimOperator(numeric: String): SimOperators.SimOperator? {
        for (simOperator in simOperators) {
            if (numeric == "${simOperator.mcc}${simOperator.mnc}") {
                return simOperator
            }
        }
        return null
    }

    class SimOperators : ArrayList<SimOperators.SimOperator>() {
        data class SimOperator(
            val bands: String?,
            val brand: String?,
            val countryCode: String?,
            val countryName: String?,
            val mcc: String?,
            val mnc: String?,
            val notes: String?,
            val operator: String?,
            val status: String?,
            val type: String?
        )
    }
}
