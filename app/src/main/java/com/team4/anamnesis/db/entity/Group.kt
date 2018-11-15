package com.team4.anamnesis.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * A grouping of flashcard decks, usually by subject.
 */
@Entity(tableName = "group")
data class Group(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        @ColumnInfo(name = "name") var name: String
): Serializable