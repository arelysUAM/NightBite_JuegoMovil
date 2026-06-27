package ni.edu.uam.nightbiteapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ni.edu.uam.nightbiteapp.data.local.dao.GameProgressDao
import ni.edu.uam.nightbiteapp.data.local.entity.BadgeEntity
import ni.edu.uam.nightbiteapp.data.local.entity.LevelResultEntity
import ni.edu.uam.nightbiteapp.data.local.entity.ProgressEntity

@Database(
    entities = [
        ProgressEntity::class,
        LevelResultEntity::class,
        BadgeEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class NightBiteDatabase : RoomDatabase() {

    abstract fun gameProgressDao(): GameProgressDao

    companion object {

        @Volatile
        private var INSTANCE: NightBiteDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS badges (
                        userId INTEGER NOT NULL,
                        levelId INTEGER NOT NULL,
                        badgeCode TEXT NOT NULL,
                        unlockedAt INTEGER NOT NULL,
                        PRIMARY KEY(userId, levelId)
                    )
                    """.trimIndent()
                )
            }
        }

        fun getDatabase(context: Context): NightBiteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NightBiteDatabase::class.java,
                    "nightbite_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}