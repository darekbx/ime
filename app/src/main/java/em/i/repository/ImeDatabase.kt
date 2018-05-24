package em.i.repository

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(Entry::class), version = 1)
abstract class ImeDatabase: RoomDatabase() {

    abstract fun getEntryDao() : EntryDao

    companion object {

        private val DB_NAME = "entry_db"

        @Volatile
        private var INSTANCE: ImeDatabase? = null

        fun getInstance(context: Context): ImeDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context)
                            .also { database ->
                                INSTANCE = database
                            }
                }

        private fun buildDatabase(context: Context) =
                Room
                        .databaseBuilder(context.applicationContext, ImeDatabase::class.java, DB_NAME)
                        .allowMainThreadQueries()
                        .build()

    }
}