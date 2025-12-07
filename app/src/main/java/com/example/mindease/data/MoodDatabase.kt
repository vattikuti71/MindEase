package com.example.mindease.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mindease.data.mindfulness.MindfulnessDao
import com.example.mindease.data.mindfulness.MindfulnessExercise

@Database(
    entities = [Mood::class, JournalEntry::class, MindfulnessExercise::class],
    version = 4,
    exportSchema = false
)
abstract class MoodDatabase : RoomDatabase() {
    // Access Mood DAO
    abstract fun moodDao(): MoodDao
    // Access Journal DAO
    abstract fun journalDao(): JournalDao
    // Access Mindfulness DAO
    abstract fun mindfulnessDao(): MindfulnessDao

    companion object {
        @Volatile
        private var INSTANCE: MoodDatabase? = null

        fun getDatabase(context: Context): MoodDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoodDatabase::class.java,
                    "mindease_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}