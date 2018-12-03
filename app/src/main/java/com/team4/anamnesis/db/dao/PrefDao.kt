package com.team4.anamnesis.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.team4.anamnesis.db.entity.Pref

@Dao
interface PrefDao {

    @Insert
    fun create(pref: Pref)

    @Delete
    fun delete(pref: Pref)

    @Query("SELECT * FROM pref ORDER BY name ASC")
    fun getAll(): LiveData<List<Pref>>

    @Query("SELECT * FROM pref WHERE name = :name LIMIT 1")
    fun getByKey(name: String): LiveData<List<Pref>>

    @Update
    fun update(pref: Pref)

}