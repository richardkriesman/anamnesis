package com.team4.anamnesis.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.team4.anamnesis.db.dao.DeckDao
import com.team4.anamnesis.db.dao.FlashcardDao
import com.team4.anamnesis.db.dao.FolderDao
import com.team4.anamnesis.db.dao.PrefDao
import com.team4.anamnesis.db.entity.Deck
import com.team4.anamnesis.db.entity.Flashcard
import com.team4.anamnesis.db.entity.Folder
import com.team4.anamnesis.db.entity.Pref

const val DATABASE_NAME = "anamnesis"

@Database(entities = [Deck::class, Flashcard::class, Folder::class, Pref::class], version = 1)
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

    abstract fun deckDao(): DeckDao
    abstract fun flashcardDao(): FlashcardDao
    abstract fun folderDao(): FolderDao
    abstract fun prefDao(): PrefDao

}