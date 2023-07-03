package moe.tabidachi.sim

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import moe.tabidachi.sim.data.database.Database
import moe.tabidachi.sim.data.database.entity.SimOperator
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var database: Database
    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate() {
        super.onCreate()
        runBlocking(Dispatchers.IO) {
            if (database.operatorDao().count() <= 0) {
                val operators = assets.open("SimOperators.json").use {
                    Json.decodeFromStream<List<SimOperator>>(it)
                }
                database.operatorDao().upsert(operators)
            }
        }
    }
}