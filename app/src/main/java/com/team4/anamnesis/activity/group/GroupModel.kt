package com.team4.anamnesis.activity.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.team4.anamnesis.db.AppDatabase
import com.team4.anamnesis.db.entity.FlashcardDeck
import com.team4.anamnesis.db.entity.FlashcardDeckGroup

class GroupModel: ViewModel() {
    lateinit var decks: LiveData<List<FlashcardDeck>>

    fun load(group: FlashcardDeckGroup) {
        decks = AppDatabase.instance!!.flashcardDeckDao().getByGroupId(group.id)
    }

    fun createDeck(deck: FlashcardDeck) {
        AppDatabase.instance!!.flashcardDeckDao().create(deck)
    }

    fun deleteDeck(deck: FlashcardDeck) {
        AppDatabase.instance!!.flashcardDeckDao().delete(deck)
    }
}