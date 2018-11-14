package com.team4.anamnesis.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.team4.anamnesis.db.entity.FlashcardDeckGroup

@Dao
interface FlashcardDeckGroupDao {

    @Insert
    fun create(group: FlashcardDeckGroup)

    @Delete
    fun delete(group: FlashcardDeckGroup)

    @Query("SELECT * FROM flashcard_deck_group")
    fun getAll(): LiveData<List<FlashcardDeckGroup>>

    @Query("SELECT * FROM flashcard_deck_group WHERE id = :id LIMIT 1")
    fun getById(id: Int): FlashcardDeckGroup

    @Update
    fun update(group: FlashcardDeckGroup)

}