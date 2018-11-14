package com.team4.anamnesis.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcard_deck")
data class FlashcardDeck(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        @ColumnInfo(name = "name") var name: String,
        @ColumnInfo(name = "group_id") var groupId: Int
)