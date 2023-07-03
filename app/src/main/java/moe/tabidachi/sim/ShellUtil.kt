package moe.tabidachi.sim

import java.io.DataOutputStream

object ShellUtil {
    private const val SETPROP = "setprop"
    private const val PROPERTY_NUMERIC = "gsm.sim.operator.numeric"
    private const val PROPERTY_COUNTRY = "gsm.sim.operator.iso-country"

    fun enableLocationReport(numeric: String, country: String) = runCatching {
        val process = Runtime.getRuntime().exec("su")
        val dataOutputStream = DataOutputStream(process.outputStream)
        setFakeCarrier(process, dataOutputStream, numeric, country)
        dataOutputStream.writeBytes("exit\n")
        dataOutputStream.flush()
    }.onFailure {
        it.printStackTrace()
    }

    private fun setFakeCarrier(
        process: Process,
        dataOutputStream: DataOutputStream,
        numeric: String,
        country: String
    ) = runCatching {
        dataOutputStream.writeBytes("$SETPROP $PROPERTY_NUMERIC $numeric\n")
        dataOutputStream.flush()
        dataOutputStream.writeBytes("$SETPROP $PROPERTY_COUNTRY $country\n")
        dataOutputStream.flush()
    }.onFailure {
        it.printStackTrace()
    }
}