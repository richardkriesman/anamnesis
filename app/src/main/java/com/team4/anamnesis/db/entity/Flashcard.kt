package com.team4.anamnesis.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A virtual "flashcard" with front and back faces for text.
 */
@Entity(tableName = "flashcard")
data class Flashcard(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        @ColumnInfo(name = "front_text") var frontText: String,
        @ColumnInfo(name = "back_text") var backText: String,
        @ColumnInfo(name = "deck_id") var deckId: Int
)