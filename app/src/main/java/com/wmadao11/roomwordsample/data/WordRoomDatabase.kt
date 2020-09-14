package com.wmadao11.roomwordsample.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile private var instance: WordRoomDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): WordRoomDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    WordRoomDatabase::class.java,
                    "word_database"
                ).addCallback(WordDatabaseCallback(scope))
                    .build()
                    .also { instance = it }
            }
        }
    }


    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            instance?.let { database ->
                scope.launch {
                    val wordDao = database.wordDao()
                    wordDao.insert(Word("Hello"))
                    wordDao.insert(Word("Room"))
                }
            }
        }
    }
}