package moe.tabidachi.sim.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import moe.tabidachi.sim.data.database.entity.Country

@Dao
interface CountryDao {
    @Query("select * from Country")
    fun getAllFlow(): Flow<List<Country>>
    @Upsert
    fun add(operator: Country)
    @Delete
    fun delete(operator: Country)
    @Query("select * from Country where countryCode = :countryCode")
    fun queryByCountryCode(countryCode: String): Country?
}