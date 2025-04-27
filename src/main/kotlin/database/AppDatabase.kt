package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ch.qos.logback.classic.pattern.DateConverter
import com.example.database.DAO.FlashcardDao
import com.example.database.converter.ListConverter
import com.example.database.entity.FlashcardEntity

@Database(
    entities = [FlashcardEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class, ListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao
}