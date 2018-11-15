package com.team4.anamnesis.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.team4.anamnesis.db.entity.Flashcard

@Dao
interface FlashcardDao {

    @Insert
    fun create(flashcard: Flashcard)

    @Delete
    fun delete(flashcard: Flashcard)

    @Update
    fun update(flashcard: Flashcard)

    @Query("SELECT * FROM flashcard WHERE deck_id = :deckId")
    fun getByDeckId(deckId: Int): LiveData<List<Flashcard>>

}