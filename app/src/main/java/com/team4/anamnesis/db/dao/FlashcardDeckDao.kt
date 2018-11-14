package com.team4.anamnesis.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.team4.anamnesis.db.entity.FlashcardDeck

@Dao
interface FlashcardDeckDao {

    @Insert
    fun create(flashcardDeck: FlashcardDeck)

    @Delete
    fun delete(flashcardDeck: FlashcardDeck)

    @Query("SELECT * FROM flashcard_deck")
    fun getAll(): List<FlashcardDeck>

    @Query("SELECT * FROM flashcard_deck WHERE group_id = :groupId")
    fun getByGroupId(groupId: Int): List<FlashcardDeck>

}