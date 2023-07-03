package moe.tabidachi.sim.data.database.entity

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Entity(primaryKeys = ["mcc", "mnc"])
@Serializable
data class SimOperator(
    val type: String?,
    val countryName: String?,
    val countryCode: String?,
    val mcc: String,
    val mnc: String,
    val brand: String?,
    val operator: String?,
    val status: String?,
    val bands: String?,
    val notes: String?,
)