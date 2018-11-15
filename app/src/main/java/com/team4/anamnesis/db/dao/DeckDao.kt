package com.team4.anamnesis.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.team4.anamnesis.db.entity.Deck

@Dao
interface DeckDao {

    @Insert
    fun create(deck: Deck)

    @Delete
    fun delete(deck: Deck)

    @Query("SELECT * FROM deck")
    fun getAll(): LiveData<List<Deck>>

    @Query("SELECT * FROM deck WHERE group_id = :groupId ORDER BY id ASC")
    fun getByGroupId(groupId: Int): LiveData<List<Deck>>

}