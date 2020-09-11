package com.wmadao11.roomwordsample.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile private var instance: WordRoomDatabase? = null

        fun getInstance(context: Context): WordRoomDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    WordRoomDatabase::class.java,
                    "word_database"
                ).build().also { instance = it }
            }
        }
    }
}