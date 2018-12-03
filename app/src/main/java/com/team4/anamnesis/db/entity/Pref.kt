package com.team4.anamnesis.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.Exception

/**
 * A single preference key/value pair
 */
@Entity(tableName = "pref")
data class Pref(
        @PrimaryKey var name: String,
        @ColumnInfo var value: String
) {

    fun toBoolean(): Boolean {
        return this.value.toBoolean()
    }

    fun toInt(): Int? {
        return try {
            this.value.toInt()
        } catch (ex: Exception) {
            null
        }
    }

}