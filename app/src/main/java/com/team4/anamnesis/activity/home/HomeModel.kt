package com.team4.anamnesis.activity.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.team4.anamnesis.db.AppDatabase
import com.team4.anamnesis.db.entity.Group

class HomeModel: ViewModel() {
    val groups: LiveData<List<Group>> = AppDatabase.instance!!.flashcardDeckGroupDao().getAll()

    fun createGroup(group: Group) {
        AppDatabase.instance!!.flashcardDeckGroupDao().create(group)
    }

    fun deleteGroup(group: Group) {
        AppDatabase.instance!!.flashcardDeckGroupDao().delete(group)
    }

    fun updateGroup(group: Group) {
        AppDatabase.instance!!.flashcardDeckGroupDao().update(group)
    }

}