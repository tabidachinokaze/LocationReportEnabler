package moe.tabidachi.sim.data.database.entity

import androidx.room.Entity

@Entity(primaryKeys = ["countryName", "countryCode"])
data class Country(
    val countryName: String,
    val countryCode: String
)