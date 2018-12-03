package com.team4.anamnesis.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.team4.anamnesis.db.entity.Folder

@Dao
interface FolderDao {

    @Insert
    fun create(group: Folder)

    @Delete
    fun delete(group: Folder)

    @Query("SELECT * FROM folder ORDER BY id ASC")
    fun getAll(): LiveData<List<Folder>>

    @Query("SELECT * FROM folder WHERE id = :id LIMIT 1")
    fun getById(id: Int): Folder

    @Update
    fun update(group: Folder)

}