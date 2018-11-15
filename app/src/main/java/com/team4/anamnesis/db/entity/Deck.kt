package com.team4.anamnesis.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * A collection of flashcards, usually by topic.
 */
@Entity(tableName = "deck")
data class Deck(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        @ColumnInfo(name = "name") var name: String,
        @ColumnInfo(name = "group_id") var groupId: Int
): Serializable