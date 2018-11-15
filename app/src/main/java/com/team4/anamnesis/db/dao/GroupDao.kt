package com.team4.anamnesis.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.team4.anamnesis.db.entity.Group

@Dao
interface GroupDao {

    @Insert
    fun create(group: Group)

    @Delete
    fun delete(group: Group)

    @Query("SELECT * FROM `group` ORDER BY id ASC")
    fun getAll(): LiveData<List<Group>>

    @Query("SELECT * FROM `group` WHERE id = :id LIMIT 1")
    fun getById(id: Int): Group

    @Update
    fun update(group: Group)

}