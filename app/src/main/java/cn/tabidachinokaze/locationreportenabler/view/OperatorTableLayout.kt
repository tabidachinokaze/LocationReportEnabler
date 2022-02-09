package cn.tabidachinokaze.locationreportenabler.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import cn.tabidachinokaze.locationreportenabler.R
import cn.tabidachinokaze.locationreportenabler.util.SimOperatorUtil

class OperatorTableLayout(context: Context) : TableLayout(context) {
    private val labelArray = context.resources.getStringArray(R.array.sim_operator)
    private val inflater = LayoutInflater.from(context)!!

    fun setInfo(simOperator: SimOperatorUtil.SimOperators.SimOperator): LinearLayout {
        val map = mutableMapOf<String, String>().apply {
            simOperator.apply {
                set(labelArray[0], type ?: "")
                set(labelArray[1], countryName ?: "")
                set(labelArray[2], countryCode ?: "")
                set(labelArray[3], mcc ?: "")
                set(labelArray[4], mnc ?: "")
                set(labelArray[5], brand ?: "")
                set(labelArray[6], operator ?: "")
                set(labelArray[7], status ?: "")
                set(labelArray[8], bands ?: "")
                set(labelArray[9], notes ?: "")
            }
        }
        addViews(map)
        return this
    }

    private fun addViews(array: Map<String, String>) {

        array.forEach {
            val view = inflater.inflate(R.layout.info_table_row, null) as TableRow
            view.apply {
                findViewById<TextView>(R.id.item_label).text = it.key
                findViewById<TextView>(R.id.item_content).text = it.value
            }
            addView(view)
        }
    }
}