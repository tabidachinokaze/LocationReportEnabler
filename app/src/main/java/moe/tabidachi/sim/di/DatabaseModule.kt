package moe.tabidachi.sim.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import moe.tabidachi.sim.data.database.Database
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext
        context: Context
    ): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            "database.db"
        ).build()
    }
}