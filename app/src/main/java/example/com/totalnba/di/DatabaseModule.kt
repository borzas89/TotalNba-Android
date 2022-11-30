package example.com.totalnba.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import example.com.totalnba.data.database.AppDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "totalnba.db"
        ).addMigrations(
            AppDatabase.MIGRATION_1_2,
            AppDatabase.MIGRATION_2_3
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providePlayerStatDao(database: AppDatabase) = database.playerStatDao()

    @Provides
    @Singleton
    fun provideResultDao(database: AppDatabase) = database.resultDao()

}