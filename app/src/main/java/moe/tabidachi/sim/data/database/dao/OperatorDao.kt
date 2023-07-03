package moe.tabidachi.sim.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import moe.tabidachi.sim.data.database.entity.SimOperator

@Dao
interface OperatorDao {
    @Query("select * from SimOperator")
    fun getAllFlow(): Flow<List<SimOperator>>
    @Query("select count() from SimOperator")
    fun count(): Int
    @Upsert
    fun upsert(operators: List<SimOperator>)
    @Query("select * from SimOperator where countryCode = :countryCode")
    fun queryByCountryCode(countryCode: String): List<SimOperator>
}