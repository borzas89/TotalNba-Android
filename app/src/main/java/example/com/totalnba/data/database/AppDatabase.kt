package example.com.totalnba.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import example.com.totalnba.data.database.dao.PlayerStatDao
import example.com.totalnba.data.database.dao.ResultDao
import example.com.totalnba.data.model.PlayerStat
import example.com.totalnba.data.model.Result

@Database(
    entities = [
        PlayerStat::class,
        Result::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playerStatDao(): PlayerStatDao
    abstract fun resultDao(): ResultDao

    companion object{
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `result` (`id` INTEGER NOT NULL, `matchId` TEXT NOT NULL, `matchTime` TEXT NOT NULL, `homeName` TEXT NOT NULL, `awayName` TEXT NOT NULL, `homeScore` INTEGER NOT NULL, `awayScore` INTEGER NOT NULL, PRIMARY KEY(`id`))")
            }
        }

        val MIGRATION_2_3 = object : Migration(2,3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE player_stat ADD COLUMN playerPicsId TEXT")
            }
        }
    }
}