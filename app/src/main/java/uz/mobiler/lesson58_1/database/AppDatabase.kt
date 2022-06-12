package uz.mobiler.lesson58_1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.mobiler.lesson58_1.database.dao.GenderDao
import uz.mobiler.lesson58_1.database.dao.PassportDao
import uz.mobiler.lesson58_1.database.dao.RegionDao
import uz.mobiler.lesson58_1.database.entity.Gender
import uz.mobiler.lesson58_1.database.entity.Passport
import uz.mobiler.lesson58_1.database.entity.Region

@Database(entities = [Passport::class, Region::class, Gender::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun passportDao(): PassportDao
    abstract fun regionDao(): RegionDao
    abstract fun genderDao(): GenderDao

    companion object {
        private var appDatabase: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "my_db")
                    .allowMainThreadQueries()
                    .build()
            }
            return appDatabase!!
        }
    }
}
