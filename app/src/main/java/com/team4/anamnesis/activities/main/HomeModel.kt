package com.team4.anamnesis.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.team4.anamnesis.db.AppDatabase
import com.team4.anamnesis.db.entity.FlashcardDeckGroup

class HomeModel: ViewModel() {
    val groups: LiveData<List<FlashcardDeckGroup>> = AppDatabase.instance!!.flashcardDeckGroupDao().getAll()

    fun createGroup(group: FlashcardDeckGroup) {
        AppDatabase.instance!!.flashcardDeckGroupDao().create(group)
    }

    fun deleteGroup(group: FlashcardDeckGroup) {
        AppDatabase.instance!!.flashcardDeckGroupDao().delete(group)
    }

    fun updateGroup(group: FlashcardDeckGroup) {
        AppDatabase.instance!!.flashcardDeckGroupDao().update(group)
    }

}