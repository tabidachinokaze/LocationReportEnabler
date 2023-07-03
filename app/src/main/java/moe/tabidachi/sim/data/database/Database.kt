package moe.tabidachi.sim.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import moe.tabidachi.sim.data.database.dao.CountryDao
import moe.tabidachi.sim.data.database.dao.OperatorDao
import moe.tabidachi.sim.data.database.entity.Country
import moe.tabidachi.sim.data.database.entity.SimOperator

@Database(
    entities = [SimOperator::class, Country::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun countryDao(): CountryDao
    abstract fun operatorDao(): OperatorDao
}