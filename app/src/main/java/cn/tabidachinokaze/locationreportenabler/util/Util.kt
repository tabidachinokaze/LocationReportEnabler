package cn.tabidachinokaze.locationreportenabler.util

import java.io.DataOutputStream

object Util {
    const val COUNTRY = "country"
    const val OPERATOR = "operator"
    const val INFO = "info"
    const val SWITCH_ENABLE_REPORT = "switch_enable_report"
    const val CHOICE_CLEAR_GMS = "choice_clear_gms_data"
    const val CHOICE_CLEAR_MAPS = "choice_clear_maps_data"
    const val CHOICE_REBOOT = "choice_reboot"
    const val CHOICE_HIDE = "choice_hide_from_launcher"
    private const val SETPROP = "setprop"
    private const val PROPERTY_NUMERIC = "gsm.sim.operator.numeric"
    private const val PROPERTY_COUNTRY = "gsm.sim.operator.iso-country"

    fun enableLocationReport(numeric: String, country: String) {
        val process = Runtime.getRuntime().exec("su")
        val dataOutputStream = DataOutputStream(process.outputStream)
        setFakeCarrier(process, dataOutputStream, numeric, country)
        dataOutputStream.writeBytes("exit\n")
        dataOutputStream.flush()
    }

    private fun setFakeCarrier(
        process: Process,
        dataOutputStream: DataOutputStream,
        numeric: String,
        country: String
    ) {
        dataOutputStream.writeBytes("$SETPROP $PROPERTY_NUMERIC $numeric\n")
        dataOutputStream.flush()
        dataOutputStream.writeBytes("$SETPROP $PROPERTY_COUNTRY $country\n")
        dataOutputStream.flush()
    }
}