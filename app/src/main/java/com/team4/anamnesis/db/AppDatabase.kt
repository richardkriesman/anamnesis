package com.team4.anamnesis.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.team4.anamnesis.db.dao.DeckDao
import com.team4.anamnesis.db.dao.GroupDao
import com.team4.anamnesis.db.entity.Deck
import com.team4.anamnesis.db.entity.Flashcard
import com.team4.anamnesis.db.entity.Group

const val DATABASE_NAME = "anamnesis"

@Database(entities = [Flashcard::class, Deck::class, Group::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        var instance: AppDatabase? = null
            private set(value) {
                field = value
            }

        /**
         * Initializes a global copy of the AppDatabase.
         *
         * @param context The application's context.
         */
        @Synchronized
        fun init(context: Context) {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
            }
        }

    }

    abstract fun flashcardDeckDao(): DeckDao
    abstract fun flashcardDeckGroupDao(): GroupDao

}